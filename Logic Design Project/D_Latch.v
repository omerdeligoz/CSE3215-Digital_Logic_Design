module D_Latch (
  input D,
  input CLK,
  output reg Q
);
 wire x, y;
nor(x, (D & CLK), y);
nor(y, (~D & CLK), x);

always@(*)begin
    Q <= y;
end

endmodule
