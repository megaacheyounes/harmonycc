package com.megaache.customcomponent.ui.components;

import com.megaache.customcomponent.ResourceTable;
import com.megaache.customcomponent.UiUtils;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.utils.Color;
import ohos.app.Context;

/**
 * custom round button
 * <p>
 * attribute:
 * "sbutton:show_arrow" : if true, an arrow will be shown on the right side of text, default: false
 * <p>
 * "sbutton:icon_src" : if provided, icon will be drawn on the left side of text, ex: 'ohos:icon_src="$media:icon"',
 * default: null
 * <p>
 * "sbutton:btn_text" : button text, can be resource ex: "$string:add_booking" or hardcoded text
 * <p>
 * "sbutton:btn_bg" : button background, ex: $graphic:bg_button_gray, default: {@link ResourceTable#Graphic_bg_button_red}
 * <p>
 * "sbutton:btn_bg" : button background, ex: $graphic:bg_button_gray, default: {@link ResourceTable#Graphic_bg_button_red}
 */
public class SButton extends StackLayout {
    public static class SButtonAttrsConstants {
        //if set to true, arrow will be shown on right side of the text
        public static final String SHOW_ARROW = "show_arrow";
        //if set, icon will be shown on left side of text
        public static final String ICON_SRC = "icon_src";
        //text to be shown in the button
        public static final String BTN_TEXT = "text";
        //background for the container
        public static final String BTN_BG = "btn_bg";
    }


    /**
     * root layout, parent of other components {@link SButton#textC} {@link SButton#iconC} {@link SButton#arrowC}
     */
    private DirectionalLayout root;

    /**
     * button text, shown in the center of the button
     */
    private Text textC;
    /**
     * icon component shown on the left side of the button's text
     */
    private Image iconC;
    /**
     * arrow component shown on the rigth side of button's text
     */
    private Text arrowC;

    public SButton(Context context) {
        super(context);
        init(null);
    }

    public SButton(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init(attrSet);
    }

    public SButton(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);

        init(attrSet);
    }

    private void init(AttrSet attrSet) {

        LayoutScatter.getInstance(getContext())
                .parse(ResourceTable.Layout_component_sbutton, this, true);

        root = (DirectionalLayout) findComponentById(ResourceTable.Id_button_root);
        textC = (Text) findComponentById(ResourceTable.Id_button_text);
        arrowC = (Text) findComponentById(ResourceTable.Id_button_arrow);
        iconC = (Image) findComponentById(ResourceTable.Id_button_icon);

        //set default
        setIconRes(null);
        setShowArrow(false);
        setText("Button");

        if (attrSet != null) {
            attrSet.getAttr(SButtonAttrsConstants.SHOW_ARROW).ifPresent(attr -> setShowArrow(attr.getBoolValue()));
            attrSet.getAttr(SButtonAttrsConstants.BTN_TEXT).ifPresent(attr -> setText(attr.getStringValue()));
            attrSet.getAttr(SButtonAttrsConstants.BTN_TEXT).ifPresent(attr -> setText(attr.getIntegerValue()));
            attrSet.getAttr(SButtonAttrsConstants.ICON_SRC).ifPresent(attr -> setIconRes(attr.getElement()));

            attrSet.getAttr(SButtonAttrsConstants.BTN_BG).ifPresent(attr -> setBg(attr.getElement()));
        }

        // setIconRes(ResourceTable.Media_icon);

    }


    /**
     * set button text
     *
     * @param textRes resource id of text that will be shown inside button
     */
    public void setText(int textRes) {
        if (textRes != -1) {
            this.textC.setText(textRes);
        }
    }

    /**
     * show icon on left side of the text
     *
     * @param imageElement image element ex: {@link ResourceTable#Media_phone}
     *                     if imageElement is null, the icon component will be hidden
     */
    public void setIconRes(Element imageElement) {
        if (imageElement == null) {
            UiUtils.hide(iconC);
        } else {
            imageElement.setBounds(0, 0, iconC.getRight(), iconC.getBottom());
            iconC.setImageElement(imageElement);
            UiUtils.visible(iconC);
        }
    }

    /**
     * set button text
     *
     * @param text will be shown inside button
     */
    public void setText(String text) {
        if (text != null && !text.isEmpty()) {
            this.textC.setText(text);
        }
    }

    /**
     * show arrow on right side of the text
     *
     * @param showArrow true to show an arrow, false otherwise
     */
    public void setShowArrow(boolean showArrow) {
        if (showArrow)
            UiUtils.visible(arrowC);
        else
            UiUtils.hide(arrowC);
    }

    /**
     * change button background
     *
     * @param shapeElement element that will be set as background
     */
    public void setBg(Element shapeElement) {
        root.setBackground(shapeElement);
    }


    /**
     * change button background using graphic resource.
     *
     * @param bgResId the resource id of the background, ex: {@see ResourceTable.Graphic_bg_button_gray}
     */
    public void setBg(int bgResId) {
        UiUtils.setBg(root, bgResId);
    }


    public void setTextColor(Color color) {
        textC.setTextColor(color);
    }

    @Override
    public void setClickedListener(ClickedListener listener) {
        super.setClickedListener((c) -> {
            animateTouchDown();
            getContext().getUITaskDispatcher().delayDispatch(() -> {
                animateTouchUp();
                listener.onClick(c);
            }, 300);
        });
    }

    /**
     * enable a button, by changing it color to accent (red), resetting its opacity to 1 and making it clickable
     */
    public void enableButton() {
        setAlpha(1f);
        setClickable(true);
        UiUtils.setBg(this, ResourceTable.Graphic_bg_button_red);
        setTextColor(UiUtils.getColor(getContext(), ResourceTable.Color_text_button_enabled));
    }

    /**
     * scale button up when clicked, called when the button is clicked
     */
    private void animateTouchDown() {
        setScale(1.1f, 1.1f);
        setAlpha(0.5f);
    }

    /**
     * scale button down to normal size, called when the button is clicked
     */
    private void animateTouchUp() {
        setScale(1, 1);
        setAlpha(1f);
    }


}
