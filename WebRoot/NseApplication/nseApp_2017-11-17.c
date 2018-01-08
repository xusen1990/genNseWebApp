
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "nse.h"
   
#define T1_ACL_SIZE 1024
#define T2_ACL_SIZE 1024
#define T3_LPM_SIZE 1024
#define T4_LPM_SIZE 1024

#define BYTE_COUNT(bits) (((bits)+8-1)/8)

void str2binary(char* str, int width);
void binary2str(char* in, char* out, int width);
void displayresponse(NseLookupResponse* response);
void binary2bit(char* in, char* out, int width);
void int2Bin(int in, char* out, int width);