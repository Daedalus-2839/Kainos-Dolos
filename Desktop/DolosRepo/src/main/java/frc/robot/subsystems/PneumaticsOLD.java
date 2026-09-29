// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class NewPneumatics extends SubsystemBase {
  DigitalOutput[] cannons;
  DigitalOutput power;
  DigitalOutput linUp;
  DigitalOutput linDown;
  private boolean locked = true;
  /** Creates a new Pneumatics. */
  public NewPneumatics() {
   cannons = new DigitalOutput[] {new DigitalOutput(0),new DigitalOutput(1),new DigitalOutput(2),new DigitalOutput(3),new DigitalOutput(4),new DigitalOutput(5)};
    power = new DigitalOutput(6);
    linUp = new DigitalOutput(7);
    linDown = new DigitalOutput(8);

    //starts up all cannons
    for (DigitalOutput cannon : cannons){
      cannon.set(true);
    }
    linDown.set(true);
    power.set(false);
    linUp.set(false);
  }
  /* DIO IS FLIPPED FALSE = TRUE */

  //yay for loops
  public void actuateSequentially(int times,double delaySeconds) {

    /*
     * Shoots all of dolos' cannons sequentially for a specified amount of times with as certain delay
     */
    if(!locked){
    for (int i = 0; i < times; i++){
      for (DigitalOutput cannon : cannons){
        cannon.set(false);
        Timer.delay(1.0);
        cannon.set(true);
        Timer.delay(delaySeconds);
      }
    }
  }
  }


  //overloaded for simple use, same functionality as the original
  public void actuateSequentially() {
    /*
     * Shoots all of dolos' cannons sequentially twice with a 1 second delay
     */
    if(!locked){
    for (int i = 0; i < 2; i++){
      for (DigitalOutput cannon : cannons){
        cannon.set(false);
        Timer.delay(1.0);
        cannon.set(true);
        Timer.delay(0.25);
      }
    }
  }
  }

  public void actuate(int cannonNum){
    /*
     * Shoots a targeted cannon, starting with zero
     */
    System.out.println("test");
    if(!locked){
    System.out.println("fired");
    cannons[cannonNum].set(false);
    Timer.delay(1);
    cannons[cannonNum].set(true);
    }

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
  
  public void toggleCannons(){
    System.out.println("Cannons Locked: "+!locked);
    locked = !locked;
  }
}
