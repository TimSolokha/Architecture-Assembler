     ORG 000      /Return from Interupt service routine stored here
ZRO, HEX 100      /Set this to start of program for 1st time through
     BUN SRV      /Branch to interupt service routine
     ORG 100      /Origin of Program is Hex 100
     LDA NU1      /Load first data value
     ADD NU2 I    /Add the 2nd data value
     AND NU3 I    /And the 3rd data value
     STA RS1      /store in counter
     HLT          /should never execute
SU1, BSA SUB      /Branch to Subroutine Sub
     HEX AA       / First variable passed to subroutine
     CLA          / Clear Accumulator
     CMA          /AC should hold FF
     SZE          /Should not Skip
     INC          /AC should now hold 00
     SZE          /Should Skip now
     HLT          /Should never execute this line
     INC          /AC should now be 01
     SPA          /should skip since +1 is positive
     HLT          /Should never execute this line
     CLA          / Clear Accumulator
     CMA          /AC should hold FF
     SNA          /should skip since AC is negative
     HLT          /Should never execute this line
     STA RS4      /Store FF at rs4
CIF, SKI
     BUN CIF      /Busy wait here
     INP          /Load input character into AC
     OUT          / Output the input character
     STA RS5      /Store first input character to Result 5
COF, SKO
     BUN COF      / Busy waiting
     LDA NU1      /Load character to output
     OUT          /Write character to output register
WAT, BUN WAT      /Wait for ever in this loop
SRV, STA SAC      /Save contents of AC
     CIR          /Move   E into AC
     STA SEE      /Save contents of E
     SKI          /Check input Flag
     BUN NXT      /Its not the input flag check next
     INP          /It was the input flag input a Character
     OUT          /write character to output register
     STA RS6      /Store whatever input Character to Result 6
NXT, SKO          /Check output Flag
     BUN EXT      /Not the output flag    exit service routine
     LDA NU2      / Load character to output
     OUT          /output character
EXT, LDA SEE      /Restore E
     CIL
     LDA SAC      /Restore AC
     ION          /Turn Interupt On
     BUN ZRO I
     HLT
SAC, DEC 00
SEE, DEC 00
SUB, DEC 00       / Store return address here
     LDA SUB I    / Get first variable
     CLE          / Clear E
     CME          / Set E bit to 1
     CIR
     STA RS2      /Store 2nd result
     CIL
     INC
     STA RS3      /Store 3rd result  
     ISZ SUB      /increment return address
     BUN SUB I    /Return from Subrountine
     ORG 150 
NU1, DEC 25       /first # to add at address 150
NU2, DEC 25
NU3, DEC 25
     ORG 200
RS1, DEC 00       /Store 1st result here
RS2, DEC 00       /Store 2nd result here
RS3, DEC 00       /Store 3rd result here
RS4, DEC 00       /Store 4th result here  FF
RS5, DEC 00       /Store 5th result here
RS6, DEC 00       /Store 6th result here
     END

   
