package com.torchlight.azure;

import com.torchlight.azure.TorchlightAzurePlugin;

import com.archimatetool.editor.ui.ImageFactory;


public interface IAzureImages {
	ImageFactory ImageFactory = new ImageFactory(TorchlightAzurePlugin.INSTANCE);

    String IMGPATH = "img/"; //$NON-NLS-1$
    
    String ICON_CANVAS_BLANK = IMGPATH + "azure-blank.png"; //$NON-NLS-1$
    String ICON_CANVAS_BLOCK = IMGPATH + "block.png"; //$NON-NLS-1$
    String ICON_CANVAS_MODEL = IMGPATH + "azure.png"; //$NON-NLS-1$
    String ICON_CANVAS_STICKY = IMGPATH + "sticky.png"; //$NON-NLS-1$
    
    String ICON_NEWFILE = IMGPATH + "newfile.gif"; //$NON-NLS-1$
    
    String ICON_ACTIVE_DIRECTORY = IMGPATH + "activedirectory.png";
    String ICON_RESOURCE_GROUP = IMGPATH + "resourcegroup.png";
    
}
