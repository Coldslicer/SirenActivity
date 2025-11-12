package frc.robot.systems;

// WPILib Imports

// Third party Hardware Imports
import com.revrobotics.spark.SparkMax;

// Robot Imports
import frc.robot.TeleopInput;
import frc.robot.motors.SparkMaxWrapper;
import frc.robot.HardwareMap;
import frc.robot.systems.AutoHandlerSystem.AutoFSMState;

enum FSMState {
	// added  my states from the state machine diagram
	CONTROLLER,
	ON_OFF,
	CRESCENDO
}

public class SirenFSMSystem extends FSMSystem<FSMState> {
	/* ======================== Constants ======================== */

	/* ======================== Private variables ======================== */

	// Hardware devices should be owned by one and only one system. They must
	// be private to their owner system and may not be used elsewhere.
	private SparkMax sirenMotor;

	/* ======================== Constructor ======================== */
	/**
	 * Create FSMSystem and initialize to starting state. Also perform any
	 * one-time initialization or configuration of hardware required. Note
	 * the constructor is called only once when the robot boots.
	 */
	public SirenFSMSystem() {
		// Perform hardware init using a wrapper class
		// this is so we can see motor outputs during simulatiuons
		sirenMotor = new SparkMaxWrapper(HardwareMap.CAN_ID_SPARK_SHOOTER,
										SparkMax.MotorType.kBrushless);

		// Reset state machine
		reset();
	}

	/* ======================== Public methods ======================== */

	// overridden methods don't require javadocs
	// however, you may want to add implementation specific javadocs

	@Override
	public void reset() {
		setCurrentState(FSMState.CONTROLLER);

		// Call one tick of update to ensure outputs reflect start state
		update(null);
	}

	@Override
	public void update(TeleopInput input) {
		switch (getCurrentState()) {
			case CONTROLLER:
				handleControllerState(input);
				break;

			case ON_OFF:
				handleOnOffState(input);
				break;

			case CRESCENDO:
				handleCrescendoState(input);
				break;

			default:
				throw new IllegalStateException("Invalid state: " + getCurrentState().toString());
		}
		setCurrentState(nextState(input));
	}

	@Override
	public boolean updateAutonomous(AutoFSMState autoState) {
		return false;
	}

	/* ======================== Protected methods ======================== */

	@Override
	protected FSMState nextState(TeleopInput input) {
		switch (getCurrentState()) {
			case CONTROLLER:
				return FSMState.CONTROLLER;

			case ON_OFF:
				return FSMState.ON_OFF;

			case CRESCENDO:
				return FSMState.CRESCENDO;

			default:
				throw new IllegalStateException("Invalid state: " + getCurrentState().toString());
		}
	}

	/* ------------------------ FSM state handlers ------------------------ */
	/**
	 * Handle behavior in CONTROLLER.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 */
	private void handleControllerState(TeleopInput input) {

	}
	/**
	 * Handle behavior in ON_OFF.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 */
	private void handleOnOffState(TeleopInput input) {

	}

	/**
	 * Handle behavior in CRESCENDO.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 */
	private void handleCrescendoState(TeleopInput input) {

	}

}
