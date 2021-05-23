package com.megaache.customcomponent.ui.abilities;

import com.megaache.customcomponent.ResourceTable;
import com.megaache.customcomponent.ui.components.SButton;
import com.megaache.customcomponent.ui.components.ShapeShiftingComponent;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        SButton sButton = (SButton) findComponentById(ResourceTable.Id_sbutton);
        sButton.setClickedListener(c -> toast("you clicked the SButton!"));

        ShapeShiftingComponent shapeShiftingButton = (ShapeShiftingComponent) findComponentById(ResourceTable.Id_ssbutton);

    }

    private void toast(String msg) {
        new ToastDialog(this).setText(msg).setAlignment(LayoutAlignment.CENTER).show();
    }
}
