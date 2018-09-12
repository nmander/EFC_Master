package com.example.niklas.efc_master;

public class Igndata
{
    private String primerBulb;



    private int temperature;
    private int rpm;
    private int run_time;
    private int attachment_nbr_status = 0; //[String, Blade, Edger, Tiller, Blower, Pole Saw]
    private int trim_mode_status = 0;   //0 = normal, lite = 1
    private int stop_status = 0; //0 = Stop Button NOT pressed, 1 = Stop Button Pressed

    public Igndata() {
        this.primerBulb = "PUSH PRIMER BULB N TIMES";
        this.rpm = 0;
        this.run_time = 0;
        this.attachment_nbr_status = 0;
        this.trim_mode_status = 0;
        this.stop_status = 0;
    }

    public int getAttachment_nbr_status() {
        return attachment_nbr_status;
    }

    public void setAttachment_nbr_status(int attachment_nbr_status) {
        this.attachment_nbr_status = attachment_nbr_status;
    }

    public int getTrim_mode_status() {
        return trim_mode_status;
    }

    public void setTrim_mode_status(int trim_mode_status) {
        this.trim_mode_status = trim_mode_status;
    }

    public int getStop_status() {
        return stop_status;
    }

    public void setStop_status(int stop_status) {
        this.stop_status = stop_status;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public int getRun_time() {
        return run_time;
    }

    public void setRun_time(int run_time) {
        this.run_time = run_time;
    }

    public String getPrimerBulb() {
        return primerBulb;
    }

    public void setPrimerBulb(String primerBulb) {
        this.primerBulb = primerBulb;
    }
    public int getTemperature() {
        return temperature;
    }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
