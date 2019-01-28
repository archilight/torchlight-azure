package com.torchlight.azure.diagram;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.ui.IEditorPart;

import com.archimatetool.editor.diagram.IArchimateDiagramEditor;
import com.archimatetool.editor.diagram.ICreationFactory;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;

public class AzureDiagramModelFactory implements ICreationFactory {
    
    /**
     * Factory method for creating a new IDiagramModelArchimateObject for an IArchimateElement
     * @param element
     * @return a new IDiagramModelArchimateObject
     */
    public IDiagramModelArchimateObject createDiagramModelArchimateObject(IArchimateElement element, String azureName, String azureIcon, String azureIconLarge) {
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement(element);
        // Figure Type
        dmo.setType(Preferences.STORE.getInt(IPreferenceConstants.DEFAULT_FIGURE_PREFIX + element.eClass().getName()));
        
        // Add new bounds with a default user size
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(dmo);
        Dimension size = provider.getUserDefaultSize();
        dmo.setBounds(0, 0, size.width, size.height);
        
        dmo.setTextPosition(ITextPosition.TEXT_POSITION_TOP);
        
        
        // add the azure properties
        IProperty iconProperty = IArchimateFactory.eINSTANCE.createProperty();
        iconProperty.setKey("AzureIcon");
        iconProperty.setValue(azureIcon);
        iconProperty.eNotify(new ENotificationImpl((InternalEObject) iconProperty, Notification.ADD, EcorePackage.EATTRIBUTE, iconProperty.getValue(), null));
        dmo.getArchimateElement().getProperties().add(iconProperty);
        
        IProperty iconPropertyLarge = IArchimateFactory.eINSTANCE.createProperty();
        iconPropertyLarge.setKey("AzureIconLarge");
        iconPropertyLarge.setValue(azureIconLarge);
        iconPropertyLarge.eNotify(new ENotificationImpl((InternalEObject) iconPropertyLarge, Notification.ADD, EcorePackage.EATTRIBUTE, iconPropertyLarge.getValue(), null));
        dmo.getArchimateElement().getProperties().add(iconPropertyLarge);
        
        IProperty nameProperty = IArchimateFactory.eINSTANCE.createProperty();
        nameProperty.setKey("AzureName");
        nameProperty.setValue(azureName);
        nameProperty.eNotify(new ENotificationImpl((InternalEObject) nameProperty, Notification.ADD, EcorePackage.EATTRIBUTE, nameProperty.getValue(), null));
        dmo.getArchimateElement().getProperties().add(nameProperty);
        
        
        
        
        
        
        
        // Set user default colors as set in prefs
        ColorFactory.setDefaultColors(dmo);
        
        
        
 
        return dmo;
    }

    /**
     * Factory method for creating a new IDiagramModelArchimateConnection for an IRelationship
     * @param element
     * @return a new IDiagramModelArchimateConnection
     */
    public static IDiagramModelArchimateConnection createDiagramModelArchimateConnection(IArchimateRelationship relation) {
        IDiagramModelArchimateConnection connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        connection.setArchimateRelationship(relation);
        
        // Set user default colors as set in prefs
        ColorFactory.setDefaultColors(connection);
        
        return connection;
    }
    
    private EClass fTemplate;
    private String azureName = "empty";
    private String azureIcon = "empty";
    private String azureIconLarge = "empty";
    
    /**
     * Constructor for creating a new Ecore type model
     * @param eClass
     */
    public AzureDiagramModelFactory(EClass template, String name, String icon, String iconLarge) {
        fTemplate = template;
        azureName = new String(name);
        azureIcon = new String(icon);
        azureIconLarge = new String(iconLarge);
    }
    
    @Override
    public boolean isUsedFor(IEditorPart editor) {
        return editor instanceof IArchimateDiagramEditor;
    }
    
    @Override
    public Object getNewObject() {
        if(fTemplate == null) {
            return null;
        }
        
        Object object = IArchimateFactory.eINSTANCE.create(fTemplate);
        
        // Connection created from Relationship Template
        if(object instanceof IArchimateRelationship) {
            return createDiagramModelArchimateConnection((IArchimateRelationship)object);
        }
        
        // Archimate Diagram Object created from Archimate Element Template
        else if(object instanceof IArchimateElement) {
            IArchimateElement element = (IArchimateElement)object;
            //element.setName(ArchiLabelProvider.INSTANCE.getDefaultName(fTemplate));
            element.setName(azureName);
            return createDiagramModelArchimateObject(element, azureName, azureIcon, azureIconLarge);
        }
        
        // Group
        else if(object instanceof IDiagramModelGroup) {
            IDiagramModelGroup group = (IDiagramModelGroup)object;
            group.setName(ArchiLabelProvider.INSTANCE.getDefaultName(fTemplate));
            ColorFactory.setDefaultColors(group);
        }
        
        // Note
        else if(object instanceof IDiagramModelNote) {
            ColorFactory.setDefaultColors((IDiagramModelObject)object);
            ((IDiagramModelNote)object).setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT);
        }
        
        // Connection
        else if(object instanceof IDiagramModelConnection) {
            ColorFactory.setDefaultColors((IDiagramModelConnection)object);
        }
                
        return object;
    }

    @Override
    public Object getObjectType() {
        return fTemplate;
    }
}
