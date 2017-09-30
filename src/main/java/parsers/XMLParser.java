package parsers;

import java.io.IOException;

import containers.*;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import nu.xom.Element;
import nu.xom.Elements;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;

public class XMLParser extends AbstractParser {
    private Document XMLDoc;

    public XMLParser(String XMLPath) throws BeanConfigurationException {
        // añadir en doc que recibe path relativo
        File XMLFile = new File(".\\"+XMLPath);
        Builder bd = new Builder(false);
        try {
            XMLDoc = bd.build(XMLFile);
        } catch(ValidityException e) {
            throw new BeanConfigurationException("XML validity exception", e);
        } catch (ParsingException e) {
            throw new BeanConfigurationException("XML parsing exception.", e);
        } catch (IOException e) {
            throw new BeanConfigurationException("XML IO exception.", e);
        }
    }

    public void getBeans(AbstractBeanFactory bf) throws BeanConfigurationException {

        Element root = XMLDoc.getRootElement();

        // root must be called beans
        if(!root.getLocalName().equals("beans"))
            throw new BeanConfigurationException("The root element of XML configuration must be named \"beans\".");

        Elements beanElements = root.getChildElements();  // children elements that declare beans
        Elements properties; // children of each bean that specify bean's properties
        Element currentBean, currentProperty;
        Bean bean;
        // Configuration variables for each bean
        String id, classString, scope, postConsS, preDesS, propertyRef, propertyVal, propertyName, propertyType;
        Boolean isSingleton;
        char injectionType = '$';
        Class beanClass;
        Property prop;
        Field propertyField;
        Method postCons = null, preDes = null;
        LinkedList<Property> props = new LinkedList<Property>();
        Class[] parameterTypes;

        for(int i = 0; i < beanElements.size(); i++) {
            props.clear();
            postCons = null;
            preDes = null;

            currentBean = beanElements.get(i);
            // child elements must be called bean
            if(!currentBean.getLocalName().equals("bean"))
                throw new BeanConfigurationException("Each child element of the root of XML configuration must be named \"bean\".");

            // get id, throw exception if not specified
            id = currentBean.getAttributeValue("id");
            if(id == null)
                throw new BeanConfigurationException("No id specified for a bean in XML configuration.");

            // get class, throw exception if not specified or found
            classString = currentBean.getAttributeValue("class");
            if(classString == null)
                throw new BeanConfigurationException("No class specified for bean \""+id+"\" in XML configuration.");

            try {
                beanClass = Class.forName(classString);
            } catch (ClassNotFoundException e) {
                throw new BeanConfigurationException("Class \""+classString+"\" of bean \""+id+"\" in XML configuration not found.", e);
            }

            // get scope, defaults to singleton
            // throws exception if different from prototype or singleton
            scope = currentBean.getAttributeValue("scope");
            if(scope == null || scope.equals("singleton")) {
                isSingleton = true;
            } else if (scope.equals("prototype")) {
                isSingleton = false;
            } else {
                throw new BeanConfigurationException("Unrecognized scope \""+scope+"\" for bean \""+id+"\" in XML configuration.");
            }

            // get constructor and destructor methods, fields are not required so no exception handling necessary
            if((postConsS = currentBean.getAttributeValue("post-construct")) != null) {
                try{
                    postCons = beanClass.getDeclaredMethod(postConsS);
                }catch (NoSuchMethodException e) {
                    throw new BeanConfigurationException("Unrecognized post-construct method \""+postConsS+"\" in bean \""+id+"\" in XML configuration.",e);
                }
            }

            if((preDesS = currentBean.getAttributeValue("pre-destruct")) != null) {
                try{
                    preDes = beanClass.getDeclaredMethod(preDesS);
                }catch (NoSuchMethodException e) {
                    throw new BeanConfigurationException("Unrecognized pre-construct method \""+postConsS+"\" in bean \""+id+"\" in XML configuration.",e);
                }
            }

            // get properties
            properties = currentBean.getChildElements();
            injectionType = '$';
            for(int j = 0; j < properties.size(); j++) {
                currentProperty = properties.get(j);
                prop = new Property();

                if(currentProperty.getLocalName().equals("property") && injectionType == 'c' || currentProperty.getLocalName().equals("constructor-arg") && injectionType == 's') {
                    // injectionType is set as constructor but received a property
                    // or injectionType is set as setter but received a constructor argument
                    throw new BeanConfigurationException("Defined both constructor arguments and properties for bean \""+id+"\" in XML configuration.");
                } else if(currentProperty.getLocalName().equals("property")) {
                    // injecting with setter
                    injectionType = 's';
                }  else if(currentProperty.getLocalName().equals("constructor-arg")) {
                    // injecting with constructor
                    injectionType = 'c';
                }

                // get name of property
                propertyName = currentProperty.getAttributeValue("name");
                if (propertyName != null) {
                    prop.setName(propertyName);
                } else {
                    // properties must have a name
                    throw new BeanConfigurationException("No name specified for a property of bean \"" + id + "\" in XML configuration.");
                }

                // get class of property
                try {
                    propertyField = beanClass.getDeclaredField(propertyName);
                } catch (NoSuchFieldException e) {
                    throw new BeanConfigurationException("Property \""+propertyName+"\" undeclared in bean \""+id+"\" of class \""+beanClass.getName()+"\".",e);
                }
                prop.setType(propertyField.getType());


                // get ref/value
                propertyRef = currentProperty.getAttributeValue("ref");
                propertyVal = currentProperty.getAttributeValue("value");

                if(propertyRef == null && propertyVal == null) {
                    // each property must have either a reference or value
                    throw new BeanConfigurationException("No value or reference specified for property \""
                            +propertyName+"\" of bean \""+id+"\" in XML configuration.");
                } else if(propertyRef != null && propertyVal != null){
                    // properties can't have both reference and value
                    throw new BeanConfigurationException("Specified both reference and value for property \""
                            +propertyName+"\" of bean \""+id+"\" in XML configuration.");
                } else if(propertyRef != null) {
                    prop.setRef(propertyRef);
                    prop.setValue(null);
                } else {
                    prop.setValue(propertyVal);
                    prop.setRef(null);

                    /*// get class of property
                    try {
                        propertyField = beanClass.getDeclaredField(propertyName);
                    } catch (NoSuchFieldException e) {
                        throw new BeanConfigurationException("Property \""+propertyName+"\" undeclared in bean \""+id+"\" of class \""+beanClass.getName()+"\".",e);
                    }
                    prop.setType(propertyField.getType());*/

                    // instantiate from value given in string to the type of the field
                    // could be any java primitive + string
                    propertyType = propertyField.getType().getSimpleName();
                    switch(propertyType) {
                        case "String":
                            prop.setInstance(propertyVal);
                            break;

                        case "char":
                            if(propertyVal.length() == 1) {
                                prop.setInstance(propertyVal.charAt(0));
                            } else {
                                //TODO ERROR MESSAGE
                                throw new BeanConfigurationException("");
                            }
                            break;

                        case "boolean":
                            if(propertyVal.equals("true")) {
                                prop.setInstance(true);
                            } else if (propertyVal.equals("false")) {
                                prop.setInstance(false);
                            } else {
                                //TODO ERROR MESSAGE
                                throw new BeanConfigurationException("");
                            }
                            break;

                        case "long":
                            try {
                                prop.setInstance(Long.parseLong(propertyVal));
                            } catch(NumberFormatException e) {
                                //TODO ERROR MESSAGE
                                throw new BeanConfigurationException("",e);
                            }
                            break;

                        case "int":
                            try {
                                prop.setInstance(Integer.parseInt(propertyVal));
                            } catch(NumberFormatException e) {
                                //TODO ERROR MESSAGE
                                throw new BeanConfigurationException("",e);
                            }
                            break;

                        case "short":
                            try {
                                prop.setInstance(Short.parseShort(propertyVal));
                            } catch(NumberFormatException e) {
                                //TODO ERROR MESSAGE
                                throw new BeanConfigurationException("",e);
                            }
                            break;

                        case "byte":
                            try {
                                prop.setInstance(Byte.parseByte(propertyVal));
                            } catch(NumberFormatException e) {
                                //TODO ERROR MESSAGE
                                throw new BeanConfigurationException("",e);
                            }
                            break;

                        case "float":
                            try {
                                prop.setInstance(Float.parseFloat(propertyVal));
                            } catch(NumberFormatException e) {
                                //TODO ERROR MESSAGE
                                throw new BeanConfigurationException("",e);
                            }
                            break;

                        case "double":
                            try {
                                prop.setInstance(Double.parseDouble(propertyVal));
                            } catch(NumberFormatException e) {
                                //TODO ERROR MESSAGE
                                throw new BeanConfigurationException("",e);
                            }
                            break;

                        default:
                            throw new BeanConfigurationException("Property \""+propertyName+"\" of bean \""+id+"\" was given a value but is not a primitive.");
                    }
                }
                props.add(prop);
            }

            bean = new Bean(id, injectionType, isSingleton, beanClass, postCons, preDes, props);

            /*if(injectionType == 's') {
                for (Property p : props) {
                    for (Method method : beanClass.getMethods()) {
                        if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(p.getType()) && method.getName().contains("set")) {
                            bean.addSetter(p.getName(),method);
                        }
                    }
                }
            } else {
                // TODO AGARRAR EL CONSTRUCTOR
            }*/


            bf.addBean(id, bean);
        }

    }

}