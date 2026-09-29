// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Pneumatics extends SubsystemBase {
  private final double actuationDelay = 0.1;
  public DigitalOutput[] cannons;
  DigitalOutput power;
  DigitalOutput linUp;
  DigitalOutput linDown;
  private boolean locked = true;
  /** Creates a new Pneumatics. */
  public Pneumatics() {
    cannons = new DigitalOutput[] {new DigitalOutput(0),new DigitalOutput(1),new DigitalOutput(2),new DigitalOutput(3),new DigitalOutput(4),new DigitalOutput(5)};
    power = new DigitalOutput(6);
    linUp = new DigitalOutput(7);
    linDown = new DigitalOutput(8);

    //starts up all cannons (closes all solenoids)
    for (DigitalOutput cannon : cannons){
      cannon.set(true);
    }
    linDown.set(true);
    power.set(false);
    linUp.set(false);
  }

  /* DIO IS FLIPPED FALSE = TRUE */

  //false = open
  //true = close
  //false = always allowed
  public void setCannon(int cannonNum, boolean state){
    cannons[cannonNum].set(state);
  }

  public void linearUp() {
    power.set(true);
    linUp.set(true);
    linDown.set(true);
  }
  public void linearDown() {
    linUp.set(true);
    power.set(false);
    linDown.set(false);
  }
  public void linearStop() {
    linDown.set(true);
    power.set(false);
    linUp.set(false);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  
  //Will lock the cannons, but only if we know none of them are currently firing
  public void toggleCannons(){
    if(!isActive()){
    locked = !locked;
    System.out.println("Cannons Toggled: "+locked);
    }
  }

  //Checks each cannon to see if one is active. Used to make sure 
  private boolean isActive(){
    for (DigitalOutput cannon:cannons){
      if (!cannon.get()){
        return false;
      }
    }
    return true;
  }
  
  
  public Command actuateCannon(int cannonNum){  

    //checks if the cannons are locked or the one called is currently being fired
    if (locked || !isActive()){
      return new InstantCommand(()->System.out.println("Firing Disabled!"));
    }
    System.out.println("Firing Cannon "+cannonNum+"!");
    return Commands.sequence(new InstantCommand(()->this.setCannon(cannonNum,false)),new WaitCommand(actuationDelay),new InstantCommand(()->this.setCannon(cannonNum,true)));
  }


  //Fires each cannon sequentially,
  public Command fireSequentially(double delay){

    if (!locked){
      //creates a command list
      Command[] sequence = new Command[cannons.length*2];

      for(int i = 0; i<cannons.length;i++){
        //creates a waitcommand and actuateCannon command for each cannon
        sequence[i*2] = actuateCannon(i);
        sequence[(i*2)+1] = new WaitCommand(delay); 
      }

      return Commands.sequence(sequence);
    }

    return new InstantCommand(()->System.out.println("Firing Disabled!"));

  }
}
