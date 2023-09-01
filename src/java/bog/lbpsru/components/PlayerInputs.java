package bog.lbpsru.components;

import bog.lbpsru.components.utils.Buttons;

/**
 * @author Bog
 */
public class PlayerInputs {

    private int buttons = 0;
    private float leftX = 0;
    private float leftY = 0;
    private float rightX = 0;
    private float rightY = 0;

    public void update(int buttons, float leftX, float leftY, float rightX, float rightY)
    {
        this.buttons = buttons;
        this.leftX = leftX;
        this.leftY = leftY;
        this.rightX = rightX;
        this.rightY = rightY;
    }

    public boolean isButtonPressed(Buttons button)
    {
        return (buttons & button.getOffset()) != 0;
    }

    public float[] getLeftStick()
    {
        return new float[]{leftX, leftY};
    }

    public float[] getRightStick()
    {
        return new float[]{rightX, rightY};
    }

}
