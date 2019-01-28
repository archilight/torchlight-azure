package com.torchlight.azure.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;

import com.archimatetool.editor.propertysections.AbstractECorePropertySection;
import com.archimatetool.editor.propertysections.AbstractNameDocumentationSection;
import com.archimatetool.editor.propertysections.IObjectFilter;
import com.archimatetool.editor.propertysections.ITabbedLayoutConstants;
import com.archimatetool.editor.propertysections.ObjectFilter;
import com.archimatetool.editor.propertysections.SketchElementSection.Filter;
import com.archimatetool.editor.ui.components.StyledTextControl;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ISketchModelActor;
import com.torchlight.azure.TorchlightAzurePlugin;

public class AzureElementSection extends AbstractECorePropertySection { //AbstractNameDocumentationSection {

	 private static final String HELP_ID = "com.archimatetool.help.foobar"; //$NON-NLS-1$
	/**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
        
        	if ("".equals(TorchlightAzurePlugin.azureMessages))
        		// only display if I have azureMessages
        		return false;
        	else
        		return object instanceof IArchimateDiagramModel;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IArchimateDiagramModel.class;
        }
    }
    
    private Label label;
    private Text text;

    @Override
    protected void createControls(Composite parent) {
        //super.createControls(parent);
    	
    	/*RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
        rowLayout.marginLeft = 10;
        rowLayout.marginTop = 10;
        rowLayout.spacing = 15;
        parent.setLayout(rowLayout);*/
        label = new Label(parent, 0);
        label.setText("Azure Messages:");
        GridData gd = new GridData(SWT.NONE, SWT.NONE, false, false);
        gd.widthHint = 135;
        label.setLayoutData(gd);
        
        text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY| SWT.WRAP);
        GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.widthHint = 100;
        gd.heightHint = 200;
        text.setLayoutData(gd2);
       

        
        text.setText(TorchlightAzurePlugin.azureMessages);
        
        //text.setLayoutData(new RowData(170, 160));  
        
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }

	@Override
	protected void update() {
		// show messages
		text.setText(TorchlightAzurePlugin.azureMessages);
	}
	 
	@Override
	protected void handleSelection(IStructuredSelection selection) {
	     // stop double-firing
		if(selection != getSelection()) { 
		   update();
		}  
	}	
	
	
	
	protected void notifyChanged(Notification msg) {
		
	}
    
}
