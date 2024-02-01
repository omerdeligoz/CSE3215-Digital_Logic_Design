module Jump(
input jumpSignal,
input [2:0] IR,
input [10:0] PC,
input [10:0] Address,
input ZF,
input CF,
output reg [10:0] jumpAddress
);

//decoder
reg [8:0] out;
  always @(IR) begin
    out[0] = (~IR[2] & ~IR[1] & ~IR[0]);//JUMP
    out[1] = (~IR[2] & ~IR[1] & IR[0]); //JE
    out[2] = (~IR[2] & IR[1] & ~IR[0]); //JA
    out[3] = (~IR[2] & IR[1] & IR[0]);  //JB
    out[4] = (IR[2] & ~IR[1] & ~IR[0]); //JAE
    out[5] = (IR[2] & ~IR[1] & IR[0]);  //JBE
    out[6] = (IR[2] & IR[1] & ~IR[0]);  //
    out[7] = (IR[2] & IR[1] & IR[0]);   //
  end
//adder part
reg [10:0] addressPlusPc, pcIncreased;
wire [10:0] a, b;
ElevenBitAdder f(.SRC1(PC), .SRC2(Address), .Output(a));
ElevenBitAdder g(.SRC1(PC), .SRC2(11'b1), .Output(b));
assign addressPlusPc = a;
assign pcIncreased = b;

//and gates design
wire x, y, z;

and(x, ZF, ~CF); //A=B
and(y, ~ZF, ~CF); //A>B
and(z, ~ZF, CF); //A<B


always @(*)begin
if(jumpSignal) begin
//JUMP
if(out[0]) begin
jumpAddress = addressPlusPc;
end
//JE
if(out[1])begin
 if(out[1] & x)begin
  jumpAddress = addressPlusPc;
end
else begin
  jumpAddress = pcIncreased;
end
end
//JA
if(out[2])begin
 if(out[2] & y)begin
  jumpAddress = addressPlusPc;
end
else begin
  jumpAddress = pcIncreased;
end
end
//JB
if(out[3])begin
 if(out[3] & z)begin
  jumpAddress = addressPlusPc;
end
else begin
  jumpAddress = pcIncreased;
end
end
//JAE
if(out[4])begin
 if(out[4] & (x | z))begin
  jumpAddress = addressPlusPc;
end
else begin
  jumpAddress = pcIncreased;
end
end
//JBE
if(out[5])begin
 if(out[5] & (x | y)) begin
  jumpAddress = addressPlusPc;
end
else begin
  jumpAddress = pcIncreased;
end
end
end
end
endmodule