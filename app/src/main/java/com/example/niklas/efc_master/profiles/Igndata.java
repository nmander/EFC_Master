package com.example.niklas.efc_master.profiles;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class Igndata extends BaseObservable
{
    public final byte ENGINE_RUNNING = 1; //1  stands for protocol#1
    public final byte ENGINE_NOT_RUNNING = 2; //2  stands for protocol#2
    public final byte DETAILS_STATS_PAGE = 10; //4  stands for protocol#4
    public final byte LAST_RUN_STATS_PAGE_1 = 11; //4  stands for protocol#4
    public final byte LAST_RUN_STATS_PAGE_2 = 12; //4  stands for protocol#5
    public final byte TOTAL_RUN_STATS_PAGE_1 = 13; //4  stands for protocol#6
    public final byte TOTAL_RUN_STATS_PAGE_2 = 14; //4  stands for protocol#7
    public final byte START_TEMP_STATS_PAGE = 15; //4  stands for protocol#8
    public final byte USAGE_STATS_PAGE = 16; //4  stands for protocol#9
    private String primerBulb;
    private int temperature;
    private int rpm;
    private int run_time;
    private int attachment_nbr_status; //[Blade, Blower, Edger, Pole Saw, Tiller, String]
    private int trim_mode_status;   //0 = normal, lite = 1
    private int stop_status; //0 = Stop Button NOT pressed, 1 = Stop Button Pressed
    private int tps_status; // 0= Part Throttle, 1 = at idle position, 2 = WOT
    private int error_code; //0=Success, 1=Not following starting procedure,
    private int oil_life_cntr;
    private int total_run_time;
    private long total_run_date;


    public Igndata() {
        this.primerBulb = "PUSH PRIMER BULB N TIMES";
        this.rpm = 0;
        this.run_time = 0;
        this.attachment_nbr_status = 0;
        this.trim_mode_status = 0;
        this.stop_status = 0;
        this.tps_status = 0;
        this.error_code = 0;
        this.oil_life_cntr = 0;
        this.total_run_time = 0;
        this.total_run_date = 0;
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

    @Bindable
    public int getRpm() {
        return rpm;
    }
    public void setRpm(int rpm) {
        this.rpm = rpm;
        notifyPropertyChanged(com.example.niklas.efc_master.BR.rpm);
    }

    public int getRun_time() {
        return run_time;
    }
    public void setRun_time(int run_time) { this.run_time = run_time; }

    @Bindable
    public String getPrimerBulb() {
        return primerBulb;
    }
    @Bindable
    public void setPrimerBulb(String primerBulb) {
        this.primerBulb = primerBulb;
        notifyPropertyChanged(com.example.niklas.efc_master.BR.primerBulb);
    }

    @Bindable
    public int getTemperature() {
        return temperature;
    }
    @Bindable
    public void setTemperature(int temperature) {
        this.temperature = temperature;
        notifyPropertyChanged(com.example.niklas.efc_master.BR.temperature);
    }
    public int getTps_status() {
        return tps_status;
    }
    public void setTps_status(int tps_status) {
        this.tps_status = tps_status;
    }

    public int getError_code() {
        return error_code;
    }
    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public int getOil_life_cntr() {
        return oil_life_cntr;
    }
    public void setOil_life_cntr(int oil_life_cntr) {
        this.oil_life_cntr = oil_life_cntr;
    }

    public int getTotal_run_time() {
        return total_run_time;
    }
    public void setTotal_run_time(int total_run_time) {
        this.total_run_time = total_run_time;
    }

    public long getTotal_run_date() {
        return total_run_date;
    }
    public void setTotal_run_date(long total_run_date) {
        this.total_run_date = total_run_date;
    }
}
