#pragma config WDTE = OFF

#include <xc.h>

__PROG_CONFIG(1, 0x3FE4); // configuratie pt porturile RA6 ra7

#define FRECVENTA_ACCELERATIE 200
unsigned char este_metric = 0;

unsigned char aux = 0;
unsigned long impulsuri = 0;
unsigned long timp = 0;

char cifre[10] = {0xC0, 0xF9, 0xA4, 0xB0, 0x99, 0x92, 0x82, 0xF8, 0x80, 0x90};

int initializare();
int afisare_numar(int numar);

void main(void)
{
    initializare();
    while (1)
    {
        // 2000 impulsuri pe mila
        //
        float v = ((float)impulsuri * 1800.0) / ((float)timp);
        if (este_metric == 0)
        {
            v = v * 1.60934;
        }

        // afisarea vitezei
        afisare_numar((int)v);
    }
}

int afisare_numar(int numar)
{
    if (numar > 999)
    {
        return; // Prea multe cifre, nu poate fi afisat
    }

    if (numar < 0)
    {
        return; // nu se merge cu viteza negativa
    }

    if (numar == 0)
    {
        PORTA = cifre[0];
        PORTC = cifre[0];
        PORTD = cifre[0];
    }
    else
    {
        int cifra_unitati = numar % 10;
        int cifra_zeci = (numar / 10) % 10;
        int cifra_sute = (numar / 100) % 10;
        PORTA = cifre[cifra_sute];
        PORTC = cifre[cifra_zeci];
        PORTD = cifre[cifra_unitati];
    }
}

int initializare()
{
    // input sau output
    TRISA = 0x00;
    TRISB = 0x83;
    TRISC = 0x00;
    TRISD = 0x00;
    TRISE = 0x00;

    // setez default inchis
    PORTA = 0xFF;
    PORTC = 0xFF;
    PORTD = 0xFF;

    // timer 1
    T1CON = 0b00110000; // prescalar 8
    TMR1 = FRECVENTA_ACCELERATIE;
    TMR1IE = 1; // Enable interrupt
    TMR1ON = 1; // Start Timer1
    PEIE = 1;   // Enable the Peripheral Interrupt

    aux = PORTB;

    // Interrupt on Change
    GIE = 1;
    RBIE = 1;
    ANSELH = 0x00;
    WPUB = 0x8F;
    IOCB = 0x8F;
    RBIF = 0;

    return 0;
}

void interrupt intrerupere()
{
    if (RBIF == 1)
    {
        unsigned char aux_b = PORTB; // se salveaza valoarea curenta (in cazul in care se schimba in timpul functiei)

        if ( ((aux_b & 0b00000001) == 0x1) && ((aux_b & 0b00000001) != (aux & 0b00000001))) // pentru RB0
        {
            este_metric = !este_metric; // buton chimbare unitate masura doar pe front pozitiv
            if (!este_metric)
            {
                RB5 = 1;
                RB4 = 0;
            }
            else
            {
                RB5 = 0;
                RB4 = 1;
            }
        }

        // se receptioneaza impuls
        if ( RB7 == 1)
        {
            impulsuri++;
        }

        aux = aux_b;
        RBIF = 0;
    }

    if (TMR1IF == 1)
    {
        TMR1H = 255;
        TMR1L = FRECVENTA_ACCELERATIE;
        timp += 10;

        // acceleratia e apasata, se trimite semnal impuls 
        if (RB1)
        {
            RE1 = !RB7;
        }

        TMR1IF = 0;
    }
}