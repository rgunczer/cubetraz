package com.almagems.cubetraz.scenes.menu;

import static com.almagems.cubetraz.Game.*;


public final class MenuInitData {

    public boolean reappear;
    public CubeFaceNames cubeFaceName;

    public MenuInitData() {
        cubeFaceName = CubeFaceNames.Face_Menu;
        reappear = false;
    }
    
}
