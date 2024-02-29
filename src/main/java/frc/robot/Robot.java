// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;


/** This is a demo program showing how to use Mecanum control with the MecanumDrive class. */
public class Robot extends TimedRobot {

    private boolean DEFAULT= false;
    private boolean FEED= false;
    private boolean SHOOT= false;
    private boolean SPEAKER= false;

    private boolean UP = false;
    private boolean DOWN = false;

    private Joystick joystick;
    private GenericHID controller;
    private double driveSpeed = 0.8;

    private CANSparkMax topLeft, bottomLeft, topRight, bottomRight, climberMotor;
    private CANSparkMax motorIntake, motorShooter,armMotor; 
    private RelativeEncoder armMotorEncoder,climberMotorEncoder; // From REV library
    private MecanumDrive mecDrive;

    private DigitalOutput sensor1Trig, sensor2Trig;
    private DigitalInput sensor1Echo, sensor2Echo; 

  @Override
  public void robotInit() {
    // Joysticks
    joystick = new Joystick(0);
    controller = new GenericHID(1);

    //  Mecanum Drive Motors
    topLeft = new CANSparkMax(1, MotorType.kBrushless);
    bottomLeft = new CANSparkMax(2, MotorType.kBrushless);
    topRight = new CANSparkMax(4, MotorType.kBrushless);
    bottomRight = new CANSparkMax(3, MotorType.kBrushless);
    climberMotor = new CANSparkMax(8, MotorType.kBrushless);

    mecDrive = new MecanumDrive(topLeft, bottomLeft, topRight, bottomRight);

    // Invert motors if needed
    topRight.setInverted(true);
    bottomRight.setInverted(true);

    // Additional Talon Motors
    motorIntake = new CANSparkMax(10, MotorType.kBrushless); 
    motorShooter = new CANSparkMax(11, MotorType.kBrushless); 
    armMotorEncoder = armMotor.getEncoder();
    climberMotorEncoder = climberMotor.getEncoder();
    

    armMotorEncoder.setPosition(0);
    climberMotorEncoder.setPosition(0);

    armMotor = new CANSparkMax(12, MotorType.kBrushless); 

    DEFAULT = true;
    DOWN = true;

    

    // Ultrasonic Sensors
    sensor1Trig = new DigitalOutput(0);
    sensor1Echo = new DigitalInput(1);
    sensor2Trig = new DigitalOutput(2);
    sensor2Echo = new DigitalInput(3);
  }

  @Override
  public void teleopPeriodic() {
        // Mecanum Drive
    double x = joystick.getX();
    double y = joystick.getY();
    double rotation = joystick.getZ();
    mecDrive.driveCartesian(y * driveSpeed, x * driveSpeed, rotation * driveSpeed);

    // Additional Talon Motors

    if (controller.getRawButton(5)) { 
        motorIntake.set(0.5); 
    } else {
        motorShooter.set(0);
    }

    if (controller.getRawButton(6)) { 
        motorShooter.set(0.5); 
    } else {
        motorShooter.set(0);
    }

    //testing

    if (joystick.getRawButton(10)) { 
        armMotor.set(0.5); 
    } else {
        motorShooter.set(0);
    }
    if (joystick.getRawButton(11)) { 
        climberMotor.set(0.5); 
    } else {
        motorShooter.set(0);
    }
    SmartDashboard.putNumber("climber encoder value", armMotorEncoder.getPosition());
    SmartDashboard.putNumber("arm encoder value", armMotorEncoder.getPosition());


    // Climber
    // climbControllers();
    // armControllers();

  }


  public void climbControllers(){
    if (joystick.getRawButtonPressed(5)){
      UP= true;
      DOWN= false;
    }
    if (joystick.getRawButtonPressed(6)){
      DOWN= true;
      UP = false;
    }
    
    SmartDashboard.putNumber("climber encoder value", armMotorEncoder.getPosition());
  }
  public void climbPID(){
    if (UP){
      climbOrientation(0.7,climberMotorEncoder.getPosition() , Constants.ClimbConstants.UP);
    }
    else if (DOWN){
      climbOrientation(0.7,climberMotorEncoder.getPosition() , Constants.ClimbConstants.DOWN);

    }
    else{
      climberMotor.set(0);
    }
  }
  public void climbOrientation(double power, double x, double r){
    climberMotor.set(power*((r-x)/r));
  }

  public void armControllers(){
    if (joystick.getRawButtonPressed(1)){
      DEFAULT= true;
      FEED=SHOOT=SPEAKER = false;
    }
    if (joystick.getRawButtonPressed(2)){
      FEED= true;
      DEFAULT=SHOOT=SPEAKER = false;
    }
    if (joystick.getRawButtonPressed(3)){
      SHOOT= true;
      FEED=DEFAULT=SPEAKER = false;
    }
    if (joystick.getRawButtonPressed(4)){
      SPEAKER= true;
      FEED=SHOOT=DEFAULT = false;
    }
    SmartDashboard.putNumber("arm encoder value", armMotorEncoder.getPosition());
  }
  public void ArmPID(){
    if (DEFAULT){
      ArmOrientation(0.7,armMotorEncoder.getPosition() , Constants.ShootConstants.DEFAULT);
    }
    else if (FEED){
      ArmOrientation(0.7,armMotorEncoder.getPosition() , Constants.ShootConstants.FEED);

    }
    else if (SHOOT){
      ArmOrientation(0.7,armMotorEncoder.getPosition() , Constants.ShootConstants.SHOOT);

    }
    else if (SPEAKER){
      ArmOrientation(0.7,armMotorEncoder.getPosition() , Constants.ShootConstants.SPEAKER);

    }
    else{
      armMotor.set(0);
    }
  }
  public void ArmOrientation(double power, double x, double r){
    armMotor.set(power*((r-x)/r));
  }
}
