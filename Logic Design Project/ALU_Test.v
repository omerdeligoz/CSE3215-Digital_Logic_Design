
module ALU_tb;

// ... Instantiate the ALU module (same as before) ...
reg [17:0] SRC1, SRC2;
reg [5:0] IMM;
reg [1:0] mux2_select;
reg mux1_select;
wire [17:0] Output;

ALU dut(.SRC1(SRC1), .SRC2(SRC2), .IMM(IMM), .mux2_select(mux2_select), .mux1_select(mux1_select), .Output(Output));

initial begin
  // Test case 1: Addition
  SRC1 = 18'h1234;
  SRC2 = 18'h5678;
  IMM = 6'h0;
  mux1_select = 0;
  mux2_select = 2'b00;
  #10;
  $display("Test case 1: Addition %h + %h = %h (expected: %h)", SRC1, SRC2, Output, 18'h68AC);

  // Test case 2: AND operation
  SRC1 = 18'hFFFF;
  SRC2 = 18'h00FF;
  IMM = 6'h0;
  mux1_select = 0;
  mux2_select = 2'b01;
  #10;
  $display("Test case 2: AND %h & %h = %h (expected: %h)", SRC1, SRC2, Output, 18'h00FF);

  // Test case 3: NAND operation
  SRC1 = 18'hABCD;
  SRC2 = 18'hEF01;
  IMM = 6'h0;
  mux1_select = 0;
  mux2_select = 2'b10;
  #10;
  $display("Test case 3: NAND %h ~& %h = %h (expected: %h)", SRC1, SRC2, Output, 18'h1052);

  // Test case 4: NOR operation
  SRC1 = 18'h1111;
  SRC2 = 18'h0000;
  IMM = 6'h0;
  mux1_select = 0;
  mux2_select = 2'b11;
  #10;
  $display("Test case 4: NOR %h ~| %h = %h (expected: %h)", SRC1, SRC2, Output, 18'hEEEE);

  // Test case 5: Sign extension
  SRC1 = 18'h0000;
  SRC2 = 18'h0000;
  IMM = 6'h2A; // Sign-extend to 18'hFFFFFF2A
  mux1_select = 1;
  mux2_select = 2'b00;
  #10;
  $display("Test case 5: Sign extension IMM = %h (extended: %h)", IMM, Output);

  // Test case 6: Addition with sign-extended immediate
  SRC1 = 18'h7FFF;
  SRC2 = 18'h0000;
  IMM = 6'h3F; // Sign-extend to 18'h0000003F
  mux1_select = 1;
  mux2_select = 2'b00;
  #10;
  $display("Test case 6: Addition with sign-extended IMM %h + %h = %h (expected: %h)", SRC1, IMM, Output, 18'h803E);

  // ... Add more test cases as needed ...

end

endmodule
