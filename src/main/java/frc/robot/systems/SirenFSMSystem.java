package frc.robot.systems;

// WPILib Imports

// Third party Hardware Imports
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.ClosedLoopConfig;

import edu.wpi.first.wpilibj.Timer;

import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkClosedLoopController;

// Robot Imports
import frc.robot.TeleopInput;
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

	private static final float DEFAULT_RUN_POWER = 0.1f;

	/* ======================== Private variables ======================== */

	// Hardware devices should be owned by one and only one system. They must
	// be private to their owner system and may not be used elsewhere.
	private SparkMax sirenMotor;
	private SparkClosedLoopController pid;
	private Timer timer = new Timer();

	/* ======================== Constructor ======================== */
	/**
	 * Create FSMSystem and initialize to starting state. Also perform any
	 * one-time initialization or configuration of hardware required. Note
	 * the constructor is called only once when the robot boots.
	 */
	public SirenFSMSystem() {
		// Perform hardware init using a wrapper class
		// this is so we can see motor outputs during simulatiuons
		sirenMotor = new SparkMax(HardwareMap.CAN_ID_SPARK_SIREN,
										SparkMax.MotorType.kBrushless);

		pid = sirenMotor.getClosedLoopController();
		var config = new ClosedLoopConfig();
		config.pidf(0.00001, 0.001, 0.0005, 0.002, ClosedLoopSlot.kSlot0);

		// Reset state machine
		reset();
	}

	/* ======================== Public methods ======================== */

	// overridden methods don't require javadocs
	// however, you may want to add implementation specific javadocs

	@Override
	public void reset() {
		setCurrentState(FSMState.CONTROLLER);
		timer.restart();

		// Call one tick of update to ensure outputs reflect start state
		update(null);
	}

	@Override
	public void update(TeleopInput input) {
		if (input == null) {
			return;
		}
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
		sirenMotor.set(DEFAULT_RUN_POWER);
		return false;
	}

	/* ======================== Protected methods ======================== */

	@Override
	protected FSMState nextState(TeleopInput input) {
		if (input == null) {
			return FSMState.CONTROLLER;
		}
		switch (getCurrentState()) {
			case CONTROLLER:
				if (input.isCrescendoButtonPressed() && !input.isOnOffButtonPressed()) {
					timer.reset();
					return FSMState.CRESCENDO;
				}
				if (input.isOnOffButtonPressed() && !input.isCrescendoButtonPressed()) {
					timer.reset();
					return FSMState.ON_OFF;
				}
				return FSMState.CONTROLLER;

			case ON_OFF:
				if (input.isOnOffButtonReleased()) {
					return FSMState.CONTROLLER;
				}
				return FSMState.ON_OFF;

			case CRESCENDO:
				if (input.isCrescendoButtonReleased()) {
					return FSMState.CONTROLLER;
				}
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
		sirenMotor.set(input.getManualPitchInput());
	}

	/**
	 * Handle behavior in ON_OFF.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 */
	private void handleOnOffState(TeleopInput input) {
		if (timer.get() > 2) {
			if (sirenMotor.get() > 0) {
				sirenMotor.set(0);
			} else {
				sirenMotor.set(DEFAULT_RUN_POWER);
			}
			timer.reset();
		}
	}

	/**
	 * Handle behavior in CRESCENDO.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 */
	private void handleCrescendoState(TeleopInput input) {
		System.out.println(sirenMotor.get());
		if (timer.get() > 2) {
			if (sirenMotor.get() > 0) {
				pid.setReference(0, ControlType.kDutyCycle, ClosedLoopSlot.kSlot0);
			} else {
				pid.setReference(DEFAULT_RUN_POWER, ControlType.kDutyCycle, ClosedLoopSlot.kSlot0);
			}
			timer.reset();
		}
	}

}
