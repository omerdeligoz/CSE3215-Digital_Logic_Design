module ControlUnit(
input clock,
input clear,
input [17:0] instruction,
output reg CMP_Signal,
output reg [10:0] ADDRESS,
output reg [5:0] IMM,
output reg IR,
output reg [1:0] IR2,
output reg Write_Enable,
output reg Read_Enable_1,
output reg Read_Enable_2,
output reg MemWrite,
output reg MemRead,
output reg JumpSignal,
output reg [2:0] IR3,
output reg FetchInstruction,
output reg PCwrite,
output reg InstrRead,
output reg PCread,
output reg [3:0] WriteReg,
output reg [3:0] ReadReg1,
output reg [3:0] ReadReg2
);

wire [3:0] reg3, reg2, reg1, FSMout;
wire ADD, ADDI, AND, ANDI, NAND, NOR, LD, ST,CMP;
wire sixBitOr,fourBitOr, twoBitOr;
reg dADD, dAND, dNAND, dNOR, dLD1, dST, dCMP, dJUMP, dFetch, dLD2;

assign sixBitOr = (ADD | ADDI | AND | ANDI | NAND | NOR);
assign fourBitOr = (ADD | AND | NAND | NOR);
assign twoBitOr = (ST | CMP);

assign reg1 = instruction[14:11];
assign reg2 = instruction[10:7];
assign reg3 = instruction[3:0];

assign ADD =(~instruction[17] & ~instruction[16] & ~instruction[15] & ~instruction[6]);
assign ADDI =(~instruction[17] & ~instruction[16] & ~instruction[15] & instruction[6]);
assign AND =(~instruction[17] & ~instruction[16] & instruction[15] & ~instruction[6]);
assign ANDI =(~instruction[17] & ~instruction[16] & instruction[15] & instruction[6]);
assign NAND =(~instruction[17] & instruction[16] & ~instruction[15]);
assign NOR =(~instruction[17] & instruction[16] & instruction[15]);
assign LD =(instruction[17] & ~instruction[16] & ~instruction[15]);
assign ST =(instruction[17] & ~instruction[16] & instruction[15]);
assign CMP =(instruction[17] & instruction[16] & ~instruction[15]);

FSM one(.OPCODE(instruction[17:15]), .Clock(clock), .Clear(clear), .Output(FSMout));

always @(*)begin
case(FSMout)
4'b0000: dADD = 1'b1;
4'b0001: dAND = 1'b1;
4'b0010: dNAND = 1'b1;
4'b0011: dNOR = 1'b1;
4'b0100: dLD1 = 1'b1;
4'b0101: dST = 1'b1;
4'b0110: dCMP = 1'b1;
4'b0111: dJUMP = 1'b1;
4'b1000: dFetch = 1'b1;
4'b1001: dLD2 = 1'b1;
default: dADD = 1'b0;
endcase
end

//all outputs 
always @(*) begin
 CMP_Signal = dCMP;
end
always @(*) begin
if(dLD1 | dST | dJUMP | dLD2)
 ADDRESS = instruction[10:0];
end
always @(*) begin
if(dADD | dAND)
 IMM = instruction[10:0];
end
always @(*) begin
IR = ((dADD | dAND) & instruction[6]);
end
always @(*) begin
if(dADD | dAND | dNAND | dNOR)
IR2 = instruction[16:15];
end
always @(*) begin
 Write_Enable = (dADD | dAND | dNAND | dNOR | dLD2);
 Read_Enable_1 = (dADD | dAND | dNAND | dNOR | dST | dCMP);
 Read_Enable_2 = (dNAND | dNOR | dCMP | (~instruction[6] & dADD) | (~instruction[6] & dAND));
 MemWrite = dST;
 MemRead = (dLD1 | dLD2);
 JumpSignal = dJUMP;
end
always @(*) begin
if(dJUMP)
IR3 = instruction[13:11];
end
always @(*) begin
 FetchInstruction = dFetch;
end
always @(*) begin
 PCwrite = (dADD | dAND | dNAND | dNOR | dST | dCMP | dJUMP | dLD2);
end
always @(*) begin
 InstrRead = 1'b1;
 PCread = 1'b1;
end

always @(instruction)begin
//first or for reg write
if(ADD | ADDI | AND | ANDI | NAND | NOR | LD)begin
  WriteReg = reg1;
end
end

always @(instruction)begin
//mux readReg1 enable 01 reg2 10 reg1
if(twoBitOr | sixBitOr)begin
  if((twoBitOr == 0) & (sixBitOr == 1))
     ReadReg1 = reg2;
  
  else if((twoBitOr == 1) & (sixBitOr == 0))
     ReadReg1 = reg1;
  
end
end

always @(instruction)begin
//mux2 readReg2 enable 01 reg2 10 reg1
if(CMP | fourBitOr)begin
  if((fourBitOr == 0) & (CMP == 1))
     ReadReg2 <= reg2;
  
  else if((fourBitOr == 1) & (CMP == 0))
     ReadReg2 <= reg3;
  
end
end
endmodule
