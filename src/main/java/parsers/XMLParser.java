package parsers;

import java.io.IOException;

import containers.BeanFactory;
import containers.Bean;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import nu.xom.Element;
import nu.xom.Elements;

import java.io.File;

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

    // TODO: parse bean's properties
    public void getBeans(BeanFactory bf) throws BeanConfigurationException {

        Element root = XMLDoc.getRootElement();

        // root must be called beans
        if(!root.getLocalName().equals("beans"))
            throw new BeanConfigurationException("The root element of XML configuration must be named \"beans\".");

        Elements beanElements = root.getChildElements();  // children elements that declare beans
        Elements properties; // children of each bean that specify bean's properties
        Element currentBean, currentProperty;
        Bean bean;
        // Configuration variables for each bean
        String id, classString, scope, postCons, preDes, setter;
        Boolean isSingleton;
        Class beanClass;

        for(int i = 0; i < beanElements.size(); i++) {
            id = null;
            classString = null;
            scope= null;
            postCons = null;
            preDes = null;
            setter = null;
            beanClass = null;

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
                throw new BeanConfigurationException("No class specified for bean "+id+" in XML configuration.");
            try {
                beanClass = Class.forName(classString);
            } catch (ClassNotFoundException e) {
                throw new BeanConfigurationException("Class "+classString+" of bean "+id+" in XML configuration not found.", e);
            }

            // get scope, defaults to singleton
            // throws exception if different from prototype or singleton
            scope = currentBean.getAttributeValue("scope");
            if(scope == null || scope.equals("singleton")) {
                isSingleton = true;
            } else if (scope.equals("prototype")) {
                isSingleton = false;
            } else {
                throw new BeanConfigurationException("Unrecognized scope "+scope+" for bean "+id+" in XML configuration.");
            }

            // get constructor and destructor methods, fields are not required so no exception handling necessary
            postCons = currentBean.getAttributeValue("post-construct");
            preDes = currentBean.getAttributeValue("pre-destroy");

            // get properties
            properties = currentBean.getChildElements();
            for(int j = 0; j < properties.size(); j++) {
                // TODO: get property logic
            }

            // TODO: remove null parameter
            bean = new Bean(id, isSingleton, beanClass, postCons, preDes, null);
            bf.addBean(id, bean);
        }
    }

}