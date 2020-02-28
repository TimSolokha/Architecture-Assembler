     ORG 100         /Origin of Program is Hex 100
     LDA ADS         /Load first address of operands
     STA PTR         /Store in Pointer
     LDA NBR         /Load minus 10
     STA CTR         /stor in counter
     CLA             /Clear Accumulator
LOP, ADD PTR I       /Add an operand to AC Indirect
     ISZ PTR         /Increment Pointer
     ISZ CTR         /Increment Counter
     BUN LOP         /Repeat Loop again
     STA SUM         /Store Sum
     HLT             /Halt
ADS, HEX 150
PTR, HEX 0
NBR, DEC -10
CTR, HEX 0
SUM, HEX 0
     ORG 150
     DEC 25          /first # to add at address 150
     DEC 50
     DEC 75
     DEC 100
     DEC 25 
     DEC 50
     DEC 75
     DEC 100
     DEC 25 
     DEC 50          /10th # to add at address 159
     END             /end of program  The sum should be 575 = 23F