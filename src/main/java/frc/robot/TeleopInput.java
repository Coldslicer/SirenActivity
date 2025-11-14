package frc.robot;

// WPILib Imports
import edu.wpi.first.wpilibj.PS4Controller;

/**
 * Common class for providing driver inputs during Teleop.
 *
 * This class is the sole owner of WPILib input objects and is responsible for
 * polling input values. Systems may query TeleopInput via its getter methods
 * for inputs by value, but may not access the internal input objects.
 */
public class TeleopInput {
	/* ======================== Constants ======================== */
	private static final int CONTROLLER_PORT = 0;

	/* ======================== Private variables ======================== */
	// Input objects
	private PS4Controller controller;

	/* ======================== Constructor ======================== */
	/**
	 * Create a TeleopInput and register input devices. Note that while inputs
	 * are registered at robot initialization, valid values will not be provided
	 * by WPILib until teleop mode.
	 */
	public TeleopInput() {
		controller = new PS4Controller(CONTROLLER_PORT);
	}

	/* ======================== Public methods ======================== */
	// Getter methods for fetch input values should be defined here.
	// Method names should be descriptive of the behavior, so the
	// control mapping is hidden from other classes.

	/* ========================== Controller =========================== */

	/**
	 * Getter for the crescendo button being pressed.
	 * @return whether the crescendo button was pressed
	 */
	public boolean isCrescendoButtonPressed() {
		return controller.getSquareButtonPressed();
	}

	/**
	 * Getter for the crescendo button being released.
	 * @return whether the crescendo button was released
	 */
	public boolean isCrescendoButtonReleased() {
		return controller.getSquareButtonPressed();
	}

	/**
	 * Getter for the on-off button being pressed.
	 * @return whether the on-off button was pressed
	 */
	public boolean isOnOffButtonPressed() {
		return controller.getSquareButtonPressed();
	}

	/**
	 * Getter for the on-off button being released.
	 * @return whether the on-off button was released
	 */
	public boolean isOnOffButtonReleased() {
		return controller.getSquareButtonPressed();
	}

	/**
	 * Getter for the pitch input axis.
	 * @return the manual pitch input
	 */
	public double getManualPitchInput() {
		return controller.getLeftY();
	}

	/* ======================== Private methods ======================== */

}
