// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.commands.TeleopDrive;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Pneumatics;
public class RobotContainer {
  //private final Joystick driver = new Joystick(0);

  private final int yAxis = 1;
  private final int zRotation = 0;
  private final double speed = 1.0;
  public final XboxController joystick0 = new XboxController(0);

  private final Drive drive = new Drive();
  private final Pneumatics pneumatics = new Pneumatics();
  private boolean goofy = false;

  Command shootCannon = new SequentialCommandGroup();

  public RobotContainer() {

    configureBindings();
  }

  private void configureBindings() {
   
    new Trigger(() -> joystick0.getAButton()).onTrue(new InstantCommand(() -> pneumatics.actuateSequentially(2)));
    //up
    new Trigger(() -> joystick0.getPOV()==0).onTrue(pneumatics.actuateCannon(0));
    //right
    new Trigger(() -> joystick0.getPOV()==270).onTrue(pneumatics.actuateCannon(1));
    //left 270
    new Trigger(() -> joystick0.getPOV()==90).onTrue(pneumatics.actuateCannon(5));
    //down
    new Trigger(() -> joystick0.getPOV()==180).onTrue(pneumatics.actuateCannon(3));

    new Trigger(() -> joystick0.getRightBumper()).onTrue(pneumatics.actuateCannon(4));

    new Trigger(() -> joystick0.getLeftBumper()).onTrue(pneumatics.actuateCannon(2));
    //toggles cannons
    new Trigger(() -> joystick0.getBButton()).onTrue(new InstantCommand(() -> pneumatics.toggleCannons()));

    new Trigger(()-> joystick0.getXButton()).onTrue(new InstantCommand(()->goofy=!goofy));

    drive.setDefaultCommand(new TeleopDrive(drive,
     () -> joystick0.getRawAxis(yAxis)*speed,
     () -> joystick0.getRawAxis(zRotation)*speed,goofy
    ));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
