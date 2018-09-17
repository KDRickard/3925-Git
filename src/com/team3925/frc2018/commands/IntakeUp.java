package com.team3925.frc2018.commands;

import com.team3925.frc2018.RobotMap;
import com.team3925.frc2018.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeUp extends Command {

	@Override
	protected void initialize() {
		RobotMap.IntakeMap.LIFT_MOTOR.disable();
		RobotMap.IntakeMap.LIFT_MOTOR.setInverted(true);
		Intake.getInstance().setAngle(180);
		System.out.println("Intake Up");// TODO Auto-generated method stub
		RobotMap.IntakeMap.LIFT_MOTOR.setInverted(false);
		super.initialize();
	}
	

	protected boolean isFinished() {
		return false;
	}

}
