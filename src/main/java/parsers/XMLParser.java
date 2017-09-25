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
import java.util.LinkedList;

public class XMLParser implements Parser {
    private Document XMLDoc;

    public XMLParser(String XMLPath) throws BeanConfigurationException {
        File XMLFile = new File(XMLPath);
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
        String id, classString, scope, postCons, preDes, propertyRef, propertyVal, propertyName;
        Boolean isSingleton;
        char injectionType = '$';
        Class beanClass;
        Property prop;
        LinkedList<Property> props = new LinkedList<Property>();

        for(int i = 0; i < beanElements.size(); i++) {
            props.clear();

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
            postCons = currentBean.getAttributeValue("post-construct");
            preDes = currentBean.getAttributeValue("pre-destroy");

            // get properties
            properties = currentBean.getChildElements();
            for(int j = 0; j < properties.size(); j++) {
                // TODO: get property logic
                currentProperty = properties.get(j);
                prop = new Property();
                injectionType = '$';

                if(currentProperty.getLocalName().equals("property") && injectionType == 'c' || currentProperty.getLocalName().equals("property") && injectionType == 's') {
                    // injectionType is set as constructor but received a property
                    // or injectionType is set as setter but received a constructor argument
                    throw new BeanConfigurationException("Defined both constructor arguments and properties for bean \""+id+"\" in XML configuration.");
                } else if(currentProperty.getLocalName().equals("property")) {
                    // injecting with setter
                    injectionType = 's';

                    // get name of property
                    propertyName = currentProperty.getAttributeValue("name");
                    if(propertyName != null) {
                        prop.setName(propertyName);
                    } else {
                        // properties must have a name
                        throw new BeanConfigurationException("No name specified for a property of bean \""+id+"\" in XML configuration.");
                    }

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
                    }
                } else if(currentProperty.getLocalName().equals("constructor-arg")) {
                    // injecting with constructor
                    injectionType = 'c';

                    propertyRef = currentProperty.getAttributeValue("ref");

                    if(propertyRef == null) {
                        throw new BeanConfigurationException("No reference provided for constructor argument of bean \""+id+"\"in XML configuration.");
                    } else {
                        prop.setRef(propertyRef);
                        prop.setValue(null);
                    }
                }

                props.add(prop);
            }

            bean = new Bean(id, injectionType, isSingleton, beanClass, postCons, preDes, props);
            bf.addBean(id, bean);
        }

    }

}