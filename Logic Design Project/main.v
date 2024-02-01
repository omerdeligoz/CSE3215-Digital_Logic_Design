module main(
input clock,
input clear
);
//instruction memory
 wire [17:0] instruction;
 reg [10:0] insMemoryInput;
//control unit
 wire CMP_Signal;
 wire [10:0] ADDRESS;
 wire [5:0] IMM;
 wire IR;
 wire [1:0] IR2;
 wire Write_Enable;
 wire Read_Enable_1;
 wire Read_Enable_2;
 wire MemWrite;
 wire MemRead;
 wire JumpSignal;
 wire [2:0] IR3;
 wire FetchInstruction;
 wire PCwrite;
 wire InstrRead;
 wire PCread;
 wire [3:0] WriteReg;
 wire [3:0] ReadReg1;
 wire [3:0] ReadReg2;
//ALU
 wire [17:0] WriteData;
//PC
 wire [10:0] PCAddress;
 reg [10:0] muxOut;
//Jump
 wire [10:0] jumpAddressdeneme; 
//register
 wire [17:0] read_data1, read_data2;
//comparator
 wire ZF, CF; 

ControlUnit a(
        .clock(clock),
        .clear(clear),
        .instruction(instruction),
        .CMP_Signal(CMP_Signal),
        .ADDRESS(ADDRESS),
        .IMM(IMM),
        .IR(IR),
        .IR2(IR2),
        .Write_Enable(Write_Enable),
        .Read_Enable_1(Read_Enable_1),
        .Read_Enable_2(Read_Enable_2),
        .MemWrite(MemWrite),
        .MemRead(MemRead),
        .JumpSignal(JumpSignal),
        .IR3(IR3),
        .FetchInstruction(FetchInstruction),
        .PCwrite(PCwrite),
        .InstrRead(InstrRead),
        .PCread(PCread),
        .WriteReg(WriteReg),
        .ReadReg1(ReadReg1),
        .ReadReg2(ReadReg2)
    );

ProgramCounter b(
   .clk(clock),
   .reset(clear),
   .enable(PCread),
   .in(muxOut),
   .pc(PCAddress)
);

ALU c(
   .SRC1(read_data1), 
   .SRC2(read_data2), 
   .IMM(IMM), 
   .mux2_select(IR2), 
   .mux1_select(IR1), 
   .Output(WriteData)
);

Comparator d(
   .CMP1(read_data1),
   .CMP2(read_data2),
   .CMPsignal(CMP_Signal),
   .clk(clock),
   .clear(clear),
   .ZF(ZF),
   .CF(CF)
);

Jump j(
   .jumpSignal(JumpSignal),
   .IR(IR3),
   .PC(PCAddress),
   .Address(ADDRESS),
   .ZF(zf),
   .CF(cf),
   .jumpAddress(jumpAddressdeneme)
);

RegisterFile rf(
   .ReadRegister1(ReadReg1),
   .ReadRegister2(ReadReg2),
   .WriteRegister(WriteReg),
   .WriteData(WriteData),
   .ReadEnable1(Read_Enable_1),
   .ReadEnable2(Read_Enable_2),
   .RegWrite(Write_Enable),
   .Clk(clock),
   .ReadData1(read_data1),
   .ReadData2(read_data2)
);

data_memory dm(
   .address(ADDRESS),
   .write_data(WriteData),
   .memorywrite(MemWrite),
   .read_data(read_data1),
   .memoryread(MemRead),
   .clock(clock)
);

instruction_memory im(
   .addr(insMemoryInput),
   .data_out(instruction)
);

always @(*)begin
if(JumpSignal)
  muxOut = jumpAddressdeneme;
else
  muxOut = PCAddress + 11'b1;
end
always @(*)begin
if(FetchInstruction)
 insMemoryInput = PCAddress;
end

endmodule

module instruction_memory(
   input [10:0] addr,
   output reg [17:0] data_out
);

reg [17:0] memory [0:15]; 

initial begin
    memory[0] = 18'b000000000000000000; 
end

always @(addr) begin
    data_out = memory[addr];
end

endmodule

module data_memory(
  input [10:0] address,
  input clock,
  input memorywrite,
  input memoryread,
  input [17:0] write_data,
  output reg [17:0] read_data
);

  reg [17:0] memory [0:15];

  always @(posedge clock) begin
    if (memorywrite)
      memory[address] <= write_data;
    end
  always @(posedge clock) begin
    if (memoryread)
      read_data <= memory[address];
  end

endmodule





