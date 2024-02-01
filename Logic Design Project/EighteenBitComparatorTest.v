
module EighteenBitComparator_Test;

reg [17:0] A, B;
wire equal, A_greater, A_less;

EighteenBitComparator dut(
.A(A),
    .B(B),
    .equal(equal),
    .A_greater(A_greater),
    .A_less(A_less)
);

initial begin
#5
// Test case 1: A = 0, B = 0
A = 18'b000000000000000000;
B = 18'b000000000000000000;
#10;
$display("Test case 1: A = %b, B = %b, equal = %b, A_greater = %b, A_less = %b", A, B, equal, A_greater, A_less);

// Test case 2: A = 1, B = 0
A = 18'b000000000000000001;
B = 18'b000000000000000000;
#10;
$display("Test case 2: A = %b, B = %b, equal = %b, A_greater = %b, A_less = %b", A, B, equal, A_greater, A_less);

// Test case 3: A = 0, B = 1
A = 18'b000000000000000000;
B = 18'b000000000000000001;
#10;
$display("Test case 3: A = %b, B = %b, equal = %b, A_greater = %b, A_less = %b", A, B, equal, A_greater, A_less);

// Test case 4: A = 1, B = 1
A = 18'b000000000000000001;
B = 18'b000000000000000001;
#10;
$display("Test case 4: A = %b, B = %b, equal = %b, A_greater = %b, A_less = %b", A, B, equal, A_greater, A_less);

// Add more test cases as needed

$finish;



end

endmodule