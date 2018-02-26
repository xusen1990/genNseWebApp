
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "nse.h"
   
#define T1_ACL_SIZE 1024

#define BYTE_COUNT(bits) (((bits)+8-1)/8)

void str2binary(char* str, int width);
void binary2str(char* in, char* out, int width);
void displayresponse(NseLookupResponse* response);
void binary2bit(char* in, char* out, int width);
void int2Bin(int in, char* out, int width);
int str2int(char* in);
void swapl(char search[]);
void filetofile(char* file);

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
    char data_buff[3000] ="";
    int i, j;
    char* pri = "";
    char* data_temp;
    char* mask_temp;
    FILE* fp;
    char* file = "";
    int num = 0 ;
    int prio = 0;
    const char* v = NseManagerDp_GetVersion();
    printf("STARGATE SDK Version: %s\n", v);
	
	// Create NSE Handler
	nseStatus = NseManagerDp_Create(&MyStargate, NSE_MODE_SINGLE_PORT);
	if (nseStatus != Nse_OK)
	{
		printf("Error: fail to create manager\n");
		goto done;
	}

	// ------------------------       Add ACL TABLE ----------------------------------------------------------
	nseStatus = NseTable_Create(&MyTableBuff);  // allocate memory for table handler
	if (nseStatus != Nse_OK)
	{
		printf("Error: fail to create ACL table\n");
		goto done;
	}

	//ACL1
	MyTableBuff->name = "T1" ;
	MyTableBuff->depth = T1_ACL_SIZE;
	MyTableBuff->width = 160 ;
	MyTableBuff->adBlock = NULL;
	nseStatus = NseManagerDp_Table_Add(MyStargate, MyTableBuff, 0, 0);
	if (nseStatus != Nse_OK)
	{
		printf("Error: fail to add  ACL table\n");
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

	MyTableInfo->table = NseManagerDp_Table_Get(MyStargate, 0, 0);
	MyTableInfo->chanMask = 1;
	MyTableInfo->keySelect = 0;
	strcpy(bitmask, "11111111111111111111111111111111111111111111111111111111111111111111111111111111");
	str2binary(bitmask, 80);
	MyTableInfo->bitmask = bitmask;
	nseStatus = NseProfile_TableInfo_Add(MyProfile, MyTableInfo, 0);
	if (nseStatus != Nse_OK)
	{
		printf("Error: fail to add search table\n");
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
	file = "nseApp_admin_2018-02-01_15-45-04-043_rule.txt";
	filetofile(file);

	num = 0;
	//------------------------Add ACL Record ----------------------------------------------------------

	fp = fopen("nseApp_admin_2018-02-01_15-45-04-043_T1.txt", "r");
	MyTable = NseManagerDp_Table_Get(MyStargate, 0, 0);
	nseStatus = NseTableRecord_Create(&MyRecordBuff);  // allocate memory for record handler

	if (nseStatus != Nse_OK)
	{
		printf("Error: fail to create acl record\n\n");
		goto done;
	}
	while(fgets(data_buff, 3000, fp) != NULL)
	{
		MyRecordBuff->format = NSE_TABLERECORD_FORMAT_DATA_MASK;
		MyRecordBuff->width =160;
		data_temp = strtok(data_buff,",");
		mask_temp = strtok(NULL,",");
		strncpy( data, data_temp, strlen(data_temp) );
		strncpy( mask, mask_temp, strlen(mask_temp) );
		pri = strtok(NULL,",");
		str2binary(data, 160);
		str2binary(mask, 160);
		prio=str2int(pri);
		MyRecordBuff->data = data;  // point data pointer in record handler to data buffer
		MyRecordBuff->mask = mask;  // point mask pointer in record handler to mask buffer
		MyRecordBuff->priority = prio;  // set priroity of this record to be 100
		MyRecordBuff->range1Lower = NSE_TABLERECORD_RANGE_LOWER_NONE;  // no range
		MyRecordBuff->range1Upper = NSE_TABLERECORD_RANGE_UPPER_NONE;  // no range
		MyRecordBuff->range2Lower = NSE_TABLERECORD_RANGE_LOWER_NONE;  // no range
		MyRecordBuff->range2Upper = NSE_TABLERECORD_RANGE_UPPER_NONE;  // no range
		nseStatus = NseTable_Record_Add(MyTable, MyRecordBuff, prio);  // Add this record to T1, id is for customer to manage record, like read or delete
		if (nseStatus != Nse_OK)
		{
			printf("Error: fail to add acl record\n\n");
			goto done;
		}

		memset(data, 0, sizeof(data));
		memset(mask, 0, sizeof(mask));
		MyRecord = NseTable_Record_Get(MyTable, prio);  // read record from database
		binary2str(MyRecord->data, data, 20);  // convert binary memory to data string
		binary2str(MyRecord->mask, mask, 20);  // convert binary memory to data string
		printf("Read record: data: %s\n", data);
		printf("             mask: %s\n", mask);
		num++;
	}
	fclose(fp);
	file = "nseApp_admin_2018-02-01_15-45-04-043_searchkey.txt";
	filetofile(file);

	//------------------------Add profile1 Search ----------------------------------------------------------

	fp = fopen("nseApp_admin_2018-02-01_15-45-04-043_profile1searchkey.txt", "r");
	nseStatus = NseLookupResponse_Create(&MyResponse);
	if (nseStatus != Nse_OK)
	{
		printf("Error: fail to create response buffer\n");
		goto done;
	}

	while(fgets(data_buff, 3000, fp) != NULL)
	{
		data_temp = data_buff;
		strncpy( data, data_temp, strlen(data_temp) );
		strcpy(search, data);  // set one search key
		str2binary(search, 160);  // convert string to binary
		nseStatus = NseManagerDp_Lookup(MyStargate, 0, 0, 0, 1, search, 20, MyResponse);  // send search request to TCAM, the response will be ready(searched), match(hit), priority
		if (nseStatus != Nse_OK)
		{
			printf("Error: fail to perform search\n");
			goto done;
		}

		displayresponse(MyResponse->readies, MyResponse->matches, MyResponse->priorities);
	}
	fclose(fp);

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

int str2int(char* in)
{
	int length = strlen(in);
	int n = 0;
	for (int i = 0; i < length-1; i++)
	{
		n = n * 10 + in[i] - '0';
	}
	return n;
}

void swapl(char search[])
{
	char tmp[8];
	for (int i = 0; i < 18 / 2; i++) {
		for (int j = 0; j < 8; j++) {
			tmp[j] = search[i * 8 + j];
			search[i * 8 + j] = search[(18 - 1 - i) * 8 + j];
			search[(18 - 1 - i) * 8 + j] = tmp[j];
		}
	}
}

void filetofile(char* file)
{
	FILE * fp = fopen(file, "r");
	FILE * fd = NULL;
	printf(file);
	char buf[1024];
	char a = '#';
	int len = 0;
	char file1[1000] = "";
	if (fp != NULL)
	{
		while (fgets(buf, sizeof(buf), fp) != NULL)
		{
			if (buf[0] == a)
			{

				if (fd != NULL)
				{
					fclose(fd);
					printf("%d", strlen(buf));
					len = strlen(buf);
					strncpy(file1, buf + 1, len - 2);

					printf(file1);
					fd = fopen(file1, "w");
				}
				else
				{
					int len = strlen(buf);
					strncpy(file1, buf + 1, len - 2);
					printf(file1);
					fd = fopen(file1, "w");
				}

			}
			else
			{
				if (fd != NULL)
				{
					fputs(buf, fd);
				}
				else
				{
					printf("lll");
				}
			}

		}
		if (fd != NULL)
		{
			fclose(fd);
		}

		fclose(fp);

	}
	return;
}