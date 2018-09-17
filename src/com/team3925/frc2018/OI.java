package com.team3925.frc2018;

import com.team3925.frc2018.commands.CloseGrabbers;
import com.team3925.frc2018.commands.DecrementAdjustElevator;
import com.team3925.frc2018.commands.DriveManual.DriveManualInput;
import com.team3925.frc2018.commands.DropCube;
import com.team3925.frc2018.commands.IncrementAdjustElevator;
import com.team3925.frc2018.commands.IntakeUp;
import com.team3925.frc2018.commands.OpenGrabbers;
import com.team3925.frc2018.commands.RunElevator;
import com.team3925.frc2018.commands.RunIntakeWheels;
import com.team3925.frc2018.commands.ShiftHigh;
import com.team3925.frc2018.commands.ShiftLow;
import com.team3925.frc2018.subsystems.Elevator;
import com.team3925.frc2018.subsystems.Elevator.ElevatorState;
import com.team3925.frc2018.subsystems.Intake;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OI implements DriveManualInput {

	private final Joystick wheel;
	private final Joystick xbox;
	private final Joystick stick;

	private static OI instance;

	private Button drivetrain_Shift;

	private Button jogElevatorTop;
	private Button jogElevatorScaleHigh;
	private Button jogElevatorScaleMiddle;
	private Button jogElevatorScaleLow;
	private Button jogElevatorSwitch;
	private Button jogElevatorBottom;
	private Trigger dropCube;
//	private Trigger shootCube;
	private Button intakeCube;
	private Button openIntake;
	private Button intakeUp;

	private Trigger tuneUp;
	private Trigger tuneDown;

	public static OI getInstance() {
		if (instance == null)
			instance = new OI();
		return instance;
	}

	private OI() {
		stick = new Joystick(0);
		wheel = new Joystick(1);
		xbox = new Joystick(2);

		jogElevatorTop = new JoystickButton(xbox, 9);
		jogElevatorScaleHigh = new JoystickButton(xbox, 4);
		jogElevatorScaleMiddle = new JoystickButton(xbox, 2);
		jogElevatorScaleLow = new JoystickButton(xbox, 1);
		jogElevatorSwitch = new JoystickButton(xbox, 3);
		jogElevatorBottom = new JoystickButton(xbox, 10);

		drivetrain_Shift = new JoystickButton(wheel, 5);

		openIntake = new JoystickButton(xbox, 5);
		intakeCube = new JoystickButton(xbox, 6);
		intakeUp = new JoystickButton(xbox, 7);
		

		dropCube = new Trigger() {
			@Override
			public boolean get() {
				return xbox.getPOV() == 180;
			}
		};

//		shootCube = new Trigger() {
//
//			@Override
//			public boolean get() {
//				return xbox.getPOV() == 0;
//			}
//		};

		tuneUp = new Trigger() {

			@Override
			public boolean get() {
				return xbox.getRawAxis(3) > 0.7;
			}
		};

		tuneDown = new Trigger() {
			@Override
			public boolean get() {
				return xbox.getRawAxis(2) > 0.7;
			}
		};

		Trigger zeroElevator = new Trigger() {

			@Override
			public boolean get() {
				return Elevator.getInstance().getLimitSwitch();
			}
		};

		
		
		zeroElevator.whenActive(new Command (){
			
			@Override
			protected void initialize() {
				Elevator.getInstance().zero();
			}
			
			@Override
			protected boolean isFinished() {
				return true;
			}
		});

		jogElevatorTop.whenPressed(new RunElevator(ElevatorState.TOP));
		jogElevatorScaleHigh.whenPressed(new RunElevator(ElevatorState.SCALE_MAX));
		jogElevatorScaleMiddle.whenPressed(new RunElevator(ElevatorState.SCALE_MED));
		jogElevatorScaleLow.whenPressed(new RunElevator(ElevatorState.SCALE_LOW));
		jogElevatorSwitch.whenPressed(new RunElevator(ElevatorState.SWITCH));
		jogElevatorBottom.whenPressed(new RunElevator(ElevatorState.BOTTOM));

		dropCube.whenActive(new DropCube());
		intakeUp.whenPressed(new Command() {
			
			@Override
			protected void initialize() {
				RobotMap.IntakeMap.LIFT_MOTOR.disable();
				RobotMap.IntakeMap.LIFT_MOTOR.setInverted(true);
				Intake.getInstance().setAngle(180);
				System.out.println("Intake Up");// TODO Auto-generated method stub
				RobotMap.IntakeMap.LIFT_MOTOR.setInverted(false);
			}
			
			
			@Override
			protected boolean isFinished() {
				// TODO Auto-generated method stub
				return true;
			}
		});
//		shootCube.whileActive(new Command() {
//			@Override
//			protected void initialize() {
//				Intake.getInstance().setAngle(0);
//			}
//			@Override
//			protected void execute() {
//			}
//			
//			@Override
//			protected void end() {
//				Intake.getInstance().setIntakeRollers(-1);
//			}
//			
//			@Override
//			protected boolean isFinished() {
//				if(Elevator.state == ElevatorState.BOTTOM) {
//					return Intake.getInstance().isAtSetpoint();
//				}else {
//					return true;
//				}
//			}
//		});
//		shootCube.whenInactive(new Command() {
//			
//			@Override
//			protected void initialize() {
//				Intake.getInstance().setIntakeRollers(0);
//				if (Elevator.state == ElevatorState.BOTTOM) {
//					Intake.getInstance().setAngle(85);
//				}
//			}
//			@Override
//			protected boolean isFinished() {
//				return true;
//			}
//		});

		drivetrain_Shift.whenPressed(new ShiftLow());
		drivetrain_Shift.whenReleased(new ShiftHigh());

		openIntake.whenPressed(new OpenGrabbers());
		openIntake.whenReleased(new CloseGrabbers());

		
		
		
		//OLD INTAKE COUBE 9/8/18
		
		intakeCube.whenPressed(new RunIntakeWheels(1));
		intakeCube.whenPressed(new Command() {
			
			@Override
			protected void initialize() {
				Intake.getInstance().setAngle(180);
				System.out.println("Down");
				Intake.getInstance().setCheck(true);
			}
			@Override
			protected boolean isFinished() {
				return true;
			}
		});
		
		intakeCube.whenReleased(new RunIntakeWheels(0));
		intakeCube.whenReleased(new Command() {
			
			@Override
			protected void initialize() {
				// TODO Auto-generated method stub
				Intake.getInstance().setAngle(90);
				System.out.println("UP");
				super.initialize();
				Intake.getInstance().setCheck(false);
			}
			
			@Override
			protected boolean isFinished() {
				// TODO Auto-generated method stub
				return false;
			}
		});

		
		//OLD INTAKE CUBE 9/8/18
		
//		intakeCube.whenReleased(new RunIntakeWheels(0));
//		intakeCube.whenInactive(new Command() {
//			
//			@Override
//			protected void initialize() {
//				Constants.isGrabbing = false;
//				Intake.getInstance().setAngle(85);
//				System.out.println("UP");
//			}
//			
//			@Override
//			protected boolean isFinished() {
//				return true;
//			}
//		});

		tuneUp.whenActive(new RunIntakeWheels(-1));
		tuneDown.whenActive(new DecrementAdjustElevator());
	}

	@Override
	public double getLeft() {
		return wheel.getRawAxis(0);
	}

	@Override
	public double getFwd() {
		return -stick.getRawAxis(1);
	}

	public double getElevator() {
		return -xbox.getRawAxis(1);
	}

	public double getLiftIntake() {
		return stick.getRawAxis(2);
	}

	public double getRawElevator() {
		return xbox.getRawAxis(5);
	}
	
	

	public boolean getTestButton() {
		return wheel.getRawButton(4);
	}
}
