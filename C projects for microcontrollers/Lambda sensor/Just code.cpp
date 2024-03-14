// CONFIG
#pragma config FOSC = HS   // Oscillator Selection bits (HS oscillator)
#pragma config WDTE = OFF  // Watchdog Timer Enable bit (WDT disabled)
#pragma config PWRTE = ON  // Power-up Timer Enable bit (PWRT enabled)
#pragma config BOREN = OFF // Brown-out Reset Enable bit (BOR disabled)
#pragma config LVP = ON    // Low-Voltage (Single-Supply) In-Circuit Serial Programming Enable bit (RB3/PGM pin has PGM function; low-voltage programming enabled)
#pragma config CPD = OFF   // Data EEPROM Memory Code Protection bit (Data EEPROM code protection off)
#pragma config WRT = OFF   // Flash Program Memory Write Enable bits (Write protection off; all program memory may be written to by EECON control)
#pragma config CP = OFF    // Flash Program Memory Code Protection bit (Code protection off)

#define _XTAL_FREQ 1000000

#define DUTY_10 10
#define DUTY_30 30
#define DUTY_50 50
#define DUTY_70 70
#define DUTY_90 90

#define GOL 0x00
#define CIFRA_4 0x66
#define CIFRA_1 0x06

#include <xc.h>

unsigned int numar_semnale_high = 0;
unsigned int numar_semnale_total = 0;
unsigned int exista_avarie = 0;

void initializare_pwm()
{
    // Durata unui ciclu
    T2CKPS0 = 1; // prescaler
    T2CKPS1 = 1;

    PR2 = 100; // ciclu   forlmula calcul: PR2 = (_XTAL_FREQ / (PWM_freq * 4 * PRESCALER)) - 1;

    // Configuratie CCP1 in mod PWM
    CCP1M3 = 1;
    CCP1M2 = 1;

    TRISC2 = 0;
}

void initializare_citire_senzor_lambda()
{
    TRISC2 = 0;
}

void initializare_digiti()
{
    TRISB = 0x00;
    TRISD = 0x00;

    PORTB = GOL;
    PORTD = GOL;
}

void initializare_lampa_avarie()
{
    TRISR0 = 1;
    RE0 = 0;
}

void duty_pwm(unsigned int duty)
{
    if (duty < 255)
    {
        CCPR1L = duty;
    }
}

void initializare_timer_citire()
{
    // Set up Timer0 for a 1ms time interval
    T0CS = 0; // Use the internal instruction cycle clock (Fosc/4)
    PSA = 0;  // Use the prescaler
    PS2 = 1;  // Set prescaler to 1:32
    PS1 = 0;
    PS0 = 1;
    TMR0 = 6;   // Initialize the timer value to achieve a 1ms time interval
    TMR0IE = 1; // Enable Timer0 interrupt
    GIE = 1;    // Enable global interrupts
}

void interrupt ISR()
{
    if (TMR0IF)
    {
        int aux = RC0;

        if (RC0)
        {
            numar_semnale_high++;
        }

        if(exista_avarie)
        {
            if(numar_semnale_total % 1000 == 0){
                RE0 = 1;
            }
            
            if((numar_semnale_total + 500) % 1000 == 0){
                RE0 = 0;
            }
        }
        else
        {
            RE0 = 0;
        }

        numar_semnale_total++;

        TMR0IF = 0;
        TMR0 = 6;
    }
}

void main()
{
    initializare_pwm();
    initializare_timer_citire();
    initializare_citire_senzor_lambda();
    initializare_digiti();
    initializare_lampa_avarie();
    duty_pwm(DUTY_70);

    do
    {
        double total = (double)numar_semnale_total;
        double high = (double)numar_semnale_high;
        double procent = high / total;

        if (procent <= 0.4)
        {
            PORTB = CIFRA_4;
            PORTD = CIFRA_1;
            exista_avarie = 1;
        }
        else
        {
            if (procent >= 0.6)
            {
                PORTB = CIFRA_4;
                PORTD = CIFRA_4;
                exista_avarie = 1;
            }
            else
            {
                PORTB = GOL;
                PORTD = GOL;
                exista_avarie = 0;
            }
        }

        __delay_ms(50);
    } while (1);
}
