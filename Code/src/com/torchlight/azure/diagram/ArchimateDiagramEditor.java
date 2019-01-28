package com.torchlight.azure.diagram;

import org.eclipse.gef.GraphicalViewer;

import com.archimatetool.editor.diagram.ArchimateDiagramEditorPalette;
import com.torchlight.azure.TorchlightAzurePlugin;

public class ArchimateDiagramEditor extends com.archimatetool.editor.diagram.ArchimateDiagramEditor {
    
    static String propertyName = "drill down";
    
    @Override
    protected void createRootEditPart(GraphicalViewer viewer) {
    	super.createRootEditPart(viewer);
    }
    
    private AzurePalette fPalette;
    
    @Override
    public ArchimateDiagramEditorPalette getPaletteRoot() {
        if(fPalette == null) {
        	TorchlightAzurePlugin.NEWVIEW = true;
            fPalette = new AzurePalette();
            setPaletteViewpoint();
            TorchlightAzurePlugin.NEWVIEW = false;
        }
        
        return fPalette;
    }
    
}