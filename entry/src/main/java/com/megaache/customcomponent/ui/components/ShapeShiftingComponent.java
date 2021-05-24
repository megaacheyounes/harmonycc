package com.megaache.customcomponent.ui.components;

import com.megaache.customcomponent.ResourceTable;
import ohos.agp.components.*;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.Point;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;



public class ShapeShiftingComponent extends Component {
    public static class CSButtonAttrsConstants {
        public static final String CSBUTTON_SHAPE = "shape";
        public static final String CSBUTTON_SHAPE_COLOR = "shape_color";
    }

    public static final String CIRCLE = "circle";
    public static final String RECTANGLE = "rectangle";
    public static final String TRIANGLE = "triangle";

    private final String[] shapes = new String[]{RECTANGLE, TRIANGLE, CIRCLE};

    /**
     * button shape, can one of {@link ShapeShiftingComponent#shapes}
     */
    private String shape;
    /**
     * the color of the shape, can be set using attribute inside layout (xml) file
     */
    private Color shapeColor;

    /**
     * used to loop through the array of shapes {@link ShapeShiftingComponent#shapes}
     */
    private int shapeIndex;

    /**
     * WIDTH of the button, used to draw shape using canvas in {@link ShapeShiftingComponent#drawShape}
     */
    public static final int WIDTH = 160;
    /**
     * HEIGHT of the button, used to draw shape using canvas in {@link ShapeShiftingComponent#drawShape}
     */
    public static final int HEIGHT = 80;

    /**
     * the y offset of the text that's shown inside the (button)
     */
    public static final int TEXT_Y_OFFSET = HEIGHT / 2 + 5;
    /**
     * size of the text that's shown inside the (button)
     */
    public static final int TEXT_SIZE_FP = 20;

    public static final int CIRCLE_RADIUS = 120;

    /**
     * paint used to draw the shapes, defines the color of the shape
     * used in {@link ShapeShiftingComponent#drawShape()}
     */
    private Paint shapePaint;
    /**
     * paint used to draw the text (shape name), it defines the color and font size of the text
     * used in {@link ShapeShiftingComponent}
     */
    private Paint textPaint;

    public ShapeShiftingComponent(Context context) {
        super(context);
        init(null);
    }

    public ShapeShiftingComponent(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init(attrSet);
    }

    public ShapeShiftingComponent(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init(attrSet);
    }

    /**
     * get the attribute values received from xml layout file, then initialize the view
     * @param attrSet
     */
    private void init(AttrSet attrSet) {
        setDefaultValues();

        if (attrSet != null) {
            attrSet.getAttr(CSButtonAttrsConstants.CSBUTTON_SHAPE)
                    .ifPresent(attr -> shape = attr.getStringValue());
            attrSet.getAttr(CSButtonAttrsConstants.CSBUTTON_SHAPE_COLOR)
                    .ifPresent(attr -> shapeColor = attr.getColorValue());
        }

        preparePaint();
        setupListener();
        drawShape();

    }

    /**
     * register click listener to change shape everytime the component is clicked
     */
    private void setupListener() {
        this.setClickedListener(c -> {
            shapeIndex++;
//            if (shapeIndex >= shapes.length)
//                shapeIndex = 0;

            shape = shapes[shapeIndex % shapes.length];

            invalidate();
            postLayout();
        });
    }

    /**
     * set the default values, in case the developer didn't specify the values in layout file (xml)
     */
    private void setDefaultValues() {
        shape = RECTANGLE;
        shapeColor = new Color(getContext().getColor(ResourceTable.Color_ssbutton_default));
        shapeIndex = 0;
    }

    /**
     * add draw task to draw the next shape of the component, and it's name (shape)
     * also show toast for the current shape
     */
    private void drawShape() {
        addDrawTask((component, canvas) -> {

            new ToastDialog(getContext())
                    .setAlignment(LayoutAlignment.CENTER)
                    .setText("shape:" + shape).show();


            switch (shape) {
                case CIRCLE: {
                    Point center = new Point(WIDTH * 1.5f, HEIGHT * 1.5f);
                    canvas.drawCircle(center, CIRCLE_RADIUS, shapePaint);
                    break;
                }
                case TRIANGLE: {
                    Path trianglePath = new Path();

                    trianglePath.moveTo(0, HEIGHT * 2); //bottom left

                    trianglePath.lineTo(WIDTH * 3, HEIGHT * 2); //bottom right

                    trianglePath.lineTo(WIDTH * 1.5f, 0); //center

                    canvas.drawPath(trianglePath, shapePaint);
                    break;
                }
                default: {
                    canvas.drawRect(0, 0, toPx(WIDTH), toPx(HEIGHT), shapePaint);
                }
            }

            canvas.drawText(textPaint, shape, toPx(WIDTH / 2 - (9 * shape.length() / 2)), toPx(TEXT_Y_OFFSET));
        });

    }

    /**
     * convert VP (visual/density pixel) to pixels
     * @param vp  value in VP (density pixel)
     * @return value in pixels
     */
    private float toPx(int vp) {
        return AttrHelper.vp2px(vp, getContext());
    }

    /**
     * initial paint objects, that are used to draw the shape and text (shape name)
     */
    private void preparePaint() {
        shapePaint = new Paint();
        shapePaint.setStyle(Paint.Style.FILL_STYLE);
        shapePaint.setColor(shapeColor);

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL_STYLE);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(AttrHelper.fp2px(TEXT_SIZE_FP, getContext()));
    }


}
