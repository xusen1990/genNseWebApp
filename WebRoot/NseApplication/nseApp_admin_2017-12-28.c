
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "nse.h"
   
#define T1_LPM_SIZE 100

#define BYTE_COUNT(bits) (((bits)+8-1)/8)

void str2binary(char* str, int width);
void binary2str(char* in, char* out, int width);
void displayresponse(NseLookupResponse* response);
void binary2bit(char* in, char* out, int width);
void int2Bin(int in, char* out, int width);

int main(int argc, const char* argv[])
{
    NseError nseStatus = Nse_OK;
    NseManagerDp* MyStargate = NULL;
    NseTable* MyTableBuff = NULL;
    NseTable* MyTable = NULL;
    NseTableRecord* MyRecord = NULL;
    NseTableRecord* MyRecordBuff = NULL;
    NseProfile* MyProfile = NULL;
    NseProfile* MyProfileBuff = NULL;
    NseProfileTableInfo* MyTableInfo = NULL;
    NseProfileKguSegment* MyKguSegment = NULL;
    NseLookupResponse* MyResponse = NULL;
    NseAdBlock* MyAD = NULL;
    NseAdBlock* MyADBuff = NULL;
    
    NseLpmTable* MyLpmTable = NULL;
    NseLpmTable* MyLpmTableBuff = NULL;
    NseLpmTableRecord* MyLpmRecord = NULL;
    NseLpmTableRecord* MyLpmRecordBuff = NULL;
    NseProfileLpmTableInfo* MyLpmTableInfo = NULL;
    char strBuff[1000] = "";
    char data[1000] = "";
    char mask[1000] = "";
	char dataLine[1000] = "";
    char maskLine[1000] = "";
    char bitmask[1000] = "";
    char search[1000] = "";
    int i, j;
    const char* v = NseManagerDp_GetVersion();
    printf("STARGATE SDK Version: %s\n", v);
	
	// Create NSE Handler
	nseStatus = NseManagerDp_Create(&MyStargate, NSE_MODE_SINGLE_PORT);
	if (nseStatus != Nse_OK)
	{
		printf("Error: fail to create manager\n");
		goto done;
	}

	// ------------------------       Add LPM TABLE ----------------------------------------------------------
	nseStatus = NseLpmTable_Create(&MyLpmTableBuff);
	if (nseStatus != Nse_OK)
	{
		printf("Error: fail to create LPM table\n");
		goto done;
	}

	//LPM1
	MyLpmTableBuff->name = "T1" ;
	MyLpmTableBuff->depth = T1_LPM_SIZE;
	MyLpmTableBuff->width = 144 ;
	MyLpmTableBuff->type = V4LPM_TABLE_TYPE;
	nseStatus = NseManagerDp_LpmTable_Add(MyStargate, MyLpmTableBuff, 0);
	if (nseStatus != Nse_OK)
	{
		printf("Error: fail to add LPM table\n");
		goto done;
	}


    // ------------------------       Add Profile ----------------------------------------------------------
    nseStatus = NseProfile_Create(&MyProfileBuff);  // allocate memory for profile handler
    if (nseStatus != Nse_OK)
    {
        printf("Error: fail to create profile\n");
        goto done;
    }
    nseStatus = NseProfileTableInfo_Create(&MyTableInfo);  // allocate memory for table infomation handler; used to set which table to be search
    if (nseStatus != Nse_OK)
    {
        printf("Error: fail to create tableInfo\n");
        goto done;
    }
    nseStatus = NseProfileLpmTableInfo_Create(&MyLpmTableInfo);
    if (nseStatus != Nse_OK)
    {
           printf("Error: fail to create lpm table info\n");
           goto done;
    }
    nseStatus = NseProfileKguSegment_Create(&MyKguSegment);  // allocate memory for kgu segment handler; used to construct key
    if (nseStatus != Nse_OK)
    {
        printf("Error: fail to create KGU\n");
        goto done;
    }

    // ------------------------       Add Profile ----------------------------------------------------------
    nseStatus = NseProfile_Create(&MyProfileBuff);  // allocate memory for profile handler
    if (nseStatus != Nse_OK)
    {
        printf("Error: fail to create profile\n");
        goto done;
    }
    nseStatus = NseProfileTableInfo_Create(&MyTableInfo);  // allocate memory for table infomation handler; used to set which table to be search
    if (nseStatus != Nse_OK)
    {
        printf("Error: fail to create tableInfo\n");
        goto done;
    }
    nseStatus = NseProfileLpmTableInfo_Create(&MyLpmTableInfo);
    if (nseStatus != Nse_OK)
    {
           printf("Error: fail to create lpm table info\n");
           goto done;
    }
    nseStatus = NseProfileKguSegment_Create(&MyKguSegment);  // allocate memory for kgu segment handler; used to construct key
    if (nseStatus != Nse_OK)
    {
        printf("Error: fail to create KGU\n");
        goto done;
    }
	//profile0
	nseStatus = NseManagerDp_Profile_Add(MyStargate, MyProfileBuff, 0, 0);
	if (nseStatus != Nse_OK)
	{
		printf("Error: fail to add profile\n");
		goto done;
	}
	MyProfile = NseManagerDp_Profile_Get(MyStargate, 0, 0);

	MyLpmTableInfo->table = NseManagerDp_LpmTable_Get(MyStargate, 0, V4LPM_TABLE_TYPE);
	MyLpmTableInfo->chanMask = 1;
	MyTableInfo->keySelect = 0;
	nseStatus = NseProfile_LpmTableInfo_Add(MyProfile, MyLpmTableInfo);
	if (nseStatus != Nse_OK)
	{
		printf("Error: fail to add search lpm table\n");
		goto done;
	}
	MyKguSegment->offset = 0;
	MyKguSegment->byteCount = 15;
	nseStatus = NseProfile_KguSegment_Add(MyProfile, 0, MyKguSegment, 0);
	if (nseStatus != Nse_OK)
	{
		printf("Error: fail to add kgu segment\n");
		goto done;
	}
	MyKguSegment->offset = 16;
	MyKguSegment->byteCount = 3;
	nseStatus = NseProfile_KguSegment_Add(MyProfile, 0, MyKguSegment, 1);
	if (nseStatus != Nse_OK)
	{
		printf("Error: fail to add kgu segment\n");
		goto done;
	}


    printf("start lock config\n");
    // Start allocation and configure the TCAM database
    nseStatus = NseManagerDp_LockConfig(MyStargate);  // configure TCAM
    if (nseStatus != Nse_OK)
    {
        printf("Error: fail to configure TCAM\n");
        goto done;
    }
    printf("finish lock config\n");

done:
    if (MyStargate) NseManagerDp_Destroy(MyStargate);
    if (MyTableBuff) NseTable_Destroy(MyTableBuff);
    if (MyRecordBuff) NseTableRecord_Destroy(MyRecordBuff);
    if (MyProfileBuff) NseProfile_Destroy(MyProfileBuff);
    if (MyTableInfo) NseProfileTableInfo_Destroy(MyTableInfo);
    if (MyKguSegment) NseProfileKguSegment_Destroy(MyKguSegment);
    if (MyResponse) NseLookupResponse_Destroy(MyResponse);
    if (MyADBuff) NseAdBlock_Destroy(MyADBuff);
    return nseStatus;
}

void str2binary(char* str, int width)
{
    int i, j;
    for (i=0; i<width/8; i++)
    {
        char byte = 0;
        for (j=0; j<8; j++)
        {
            int idx = 8*i + j;
            char value = (idx < width) 
                            ? str[idx] - '0' 
                            : 0;
            byte = byte<<1 | value;
        }
        str[i] = byte;
    }
    str[i] = '\0';
}
void binary2str(char* in, char* out, int width)
{
    int i, j;
    char buff[1024] = "";
    memcpy(buff, in, width);
    for (i=0; i<width; i++)
    {
        for (j=7; j>=0; j--)
        {
            out[8*i+j] = '0' + ((int)buff[i]&0x01);
            buff[i] = buff[i]>>1;
        }
        out[width*8] = 0;
    }
}
void displayresponse(NseLookupResponse* response)
{
    int i;
    char strBuff[300];
    printf("Channel\tReady\tMatch\tPriority\tAD\n");
    for(i = 0; i < 4; i++)
    {
        memset(strBuff, 0, sizeof(strBuff));
        binary2str(response->adValues[i], strBuff, 4);
        printf("%7d\t%5d\t%5d\t%8d\t%s\n", i, response->readies[i], response->matches[i], response->priorities[i], strBuff);
    }
}


void binary2bit(char* in, char* out, int width)
{
    int i;
    for (i=0; i<width; i++)
    {
        int2Bin((int)(in[i]), &out[8*i], 8);
    }
    out[width*8] = 0;
}

void int2Bin(int in, char* out, int width)
{
    int i;
    for (i=width-1; i>=0; --i)
    {
        out[i] = '0' + (in&0X01);
        in = in>>1;
    }
    out[width]=0;
} 