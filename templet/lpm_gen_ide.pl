#!/usr/bin/env perl

use warnings;
use strict;
use utf8;
use IO::Compress::Gzip qw(gzip $GzipError);
use Math::Complex;

use Getopt::Long qw(GetOptionsFromString);
use Getopt::Long qw(GetOptionsFromArray);
use Getopt::Long qw(:config no_ignore_case);
use Getopt::Long qw(:config pass_through);
use Term::ANSIColor qw (:constants);
use File::Basename;
use threads;
use threads::shared;
use Thread::Semaphore;
use Cwd;

my $help      = 0;         #show help
my $ip        = 0;         #kind of rules 
my $vpn       = 0;         #vpn or not 
my $size      = 1;         #size of database 
my $name      = "lpm";   #name of output file 
my $type      = 0;       #type
my $seed      = time();       #seed
my $path = "C:\\Users\\peng.zhang\\Desktop\\work\\genNseApp\\templet\\"; #path
my $inc       = 0;         #inc



use constant USAGE => <<'HERE';
    -----------------------------------------------------------------------------
    # REVISION LOG
    #-----------------------------------------------------------------------------
    2017/03/24     V0.1      Initial Version
    2017/12/01     V0.2      muzp
    -----------------------------------------------------------------------------
    # Usage
    -----------------------------------------------------------------------------
    OPTION          |DEFAULT  |DESCRIPTION    -h|help          off       Print help information. <CMD>
    -h|help          off       Print help information. <CMD>
    -ip              0         IPv4 or IPv6, default IPv4
    -vpn             0/1       Whether to add VPN or not, default not 
    -s|size          1         Data size, base unit K, default 1K
    -n|name <>       lpm       Data file name, default is lpm.in
    -t               0/1       datamask/1x
    -p               addres    address
	-inc			 0/1       random/inc
    -seed            0         seed
    -----------------------------------------------------------------------------
    # EXAMPLES
    -----------------------------------------------------------------------------
    (1). Produce 4K rules for IPv4 without VPN format 1x
    >> perl lpm_gen_ide.pl -s 4 -ip 0 -vpn 0 -t 1  -p C:\\Users\\peng.zhang\\Desktop\\work\\genNseApp\\templet\\ -name lpm -inc 0
HERE

#################################################################################
## GetOption
#################################################################################
GetOptions(
    "h|help"       => \$help,
    "ip=i"           => \$ip,
    "vpn=i"          => \$vpn,
    "s|size=i"     => \$size,
    "name=s"       => \$name,
    "t=i"            => \$type,
    "path=s"       => \$path,
    "seed=i"         => \$seed, 
    "inc=i"          => \$inc,
    
);
die USAGE if $help;

#################################################################################
## Parse Options
#################################################################################
my $rec_num;
my $ip_sec;
my $mask_num;
if( -e "${name}.in") {
   unlink "${name}.in";
}
open my $fp_ip,">","$path${name}.txt" or die "Unable to open ${name}.txt\n";

print "Produce ".($ip ? "IPv6 " : "IPv4 ")."records ".($inc ? "inc " : "random").($vpn ? "with " : "without ")." VPN Begin\n";

print "check size $size, ip $ip and name $name, vpn $vpn\n";
my %hs;
my $recf;
my $mask;
my $data;
srand($seed);
for ($rec_num = 0; $rec_num <= $size*1024-1; $rec_num++) {
    $recf = '';
    $data = '';
    $mask = '';
    if($ip eq 0) 
    {   #IPv4
        $mask_num = int(rand(33));
	   if($vpn eq 0)
       {    #without VRF
            if($inc eq 0)
          { 
			for(my $par=0; $par<32; $par++) 
            {
                $ip_sec = ($par eq 7 or $par eq 30) ? 1 : int(rand(2));
                $data = $data.$ip_sec;
                if($par < (32-$mask_num))
                {
                    $recf = $recf.$ip_sec;
                    $mask = $mask."0";
                }
                else 
                {
                    $recf = $recf."0"; 
                    $mask = $mask."1";
                }
            }
			
            # If the rule is already exist, recalculate a new one
            while($hs{$recf}) 
            {
		        $recf = '';
                $data = '';
                $mask = '';
                $mask_num = int(rand(33));
                for(my $par=0; $par<32; $par++) 
                {
                    $ip_sec = ($par eq 7 or $par eq 30) ? 1 : int(rand(2));
                    $data = $data.$ip_sec;
                    if($par < (32-$mask_num)) 
                    {
                        $recf = $recf.$ip_sec;
                        $mask = $mask."0";
                    }
                    else 
                    {
                        $recf = $recf."0"; 
                        $mask = $mask."1";
                    }
                }
				#print "identical and repeat\n";
			}
		 }
		
		else
        {
			$recf= "";
            $recf = $recf.sprintf("%08b",(($rec_num>>24)&0xFF));
            $recf = $recf.sprintf("%08b",(($rec_num>>16)&0xFF));  
            $recf = $recf.sprintf("%08b",(($rec_num>>8)&0xFF));  
            $recf = $recf.sprintf("%08b",(($rec_num)&0xFF)); 
			
			$mask = $mask.sprintf("%08b",(0&0xFF));
            $mask = $mask.sprintf("%08b",(0&0xFF));  
            $mask = $mask.sprintf("%08b",(0&0xFF));  
            $mask = $mask.sprintf("%08b",(0&0xFF));   			
             
			$data = $data.$recf;
       
        }
        $hs{$recf}++;
        for(my $par = 0; $par < 112 ; $par++)
        {
                $ip_sec = int(rand(2));
                $data = $data.$ip_sec;
                $mask = $mask."1";
        }
			
        if($type eq 0)
        {
			print $fp_ip $data.",$mask";
        }
        else
        {
            print $fp_ip $recf;
               
        }   
      }
          
                
		
        
		else 
        {
            if($inc eq 0)
          { 
            for(my $par=0; $par<48; $par++) 
            {
                $ip_sec = ($par eq 7 or $par eq 30) ? 1 : int(rand(2));
                $data = $data.$ip_sec;
                if($par < (48-$mask_num)) {
                    $recf = $recf.$ip_sec;
                    $mask = $mask."0";
                }
                else {
                    $recf = $recf."0"; 
                    $mask = $mask."1";
                }
            }
			
            # If the rule is already exist, recalculate a new one
            while($hs{$recf}) 
            {
		        $recf = '';
                $data = '';
                $mask = '';
                $mask_num = int(rand(33));
                for(my $par=0; $par<48; $par++) 
                {
                    $ip_sec = ($par eq 7 or $par eq 30) ? 1 : int(rand(2));
                    $data = $data.$ip_sec;
                    if($par < (48-$mask_num))
                    {
                        $recf = $recf.$ip_sec;
                        $mask = $mask."0";
                    }
                    else 
                    {
                        $recf = $recf."0"; 
                        $mask = $mask."1";
                    }
                }
				#print "identical and repeat\n";
			}
		  }
		  else
		  {
			$recf= "";
            $recf = $recf.sprintf("%08b",(0&0xFF));
            $recf = $recf.sprintf("%08b",(0&0xFF));
            $recf = $recf.sprintf("%08b",(($rec_num>>24)&0xFF));
            $recf = $recf.sprintf("%08b",(($rec_num>>16)&0xFF));  
            $recf = $recf.sprintf("%08b",(($rec_num>>8)&0xFF));  
            $recf = $recf.sprintf("%08b",(($rec_num)&0xFF)); 
			
			$mask = $mask.sprintf("%08b",(0&0xFF));
            $mask = $mask.sprintf("%08b",(0&0xFF));  
            $mask = $mask.sprintf("%08b",(0&0xFF));  
            $mask = $mask.sprintf("%08b",(0&0xFF)); 
			$mask = $mask.sprintf("%08b",(0&0xFF));  
            $mask = $mask.sprintf("%08b",(0&0xFF));	

			$data = $recf;			
		  }
            $hs{$recf}++;
            for(my $par = 0; $par < 96; $par++)
            {
                $ip_sec = int(rand(2));
                $data = $data.$ip_sec;
                $mask = $mask."1";
            }
            if($type eq 0)
            {
                print $fp_ip $data.",$mask";
            }
            else
            {
                print $fp_ip $recf;
              
            }
        }
        
        printf $fp_ip ("\,%d\n", $rec_num);
    }
    else 
    {    # IPv6
        $mask_num = int(rand(129));
      
        if($vpn eq 0) 
		{
		  if($inc eq 0)
		 { 
            for(my $par=0; $par<128; $par++) 
            {
                $ip_sec = int(rand(2));
                $data = $data.$ip_sec;
                if($par < (128-$mask_num)) 
                {
                    $recf = $recf.$ip_sec;
                    $mask = $mask."0";
                } else 
                {
                    $recf = $recf."0";
                    $mask = $mask."1";
                }
				
				$data = $recf;
            }
			
            # If the rule is already exist, recalculate a new one
            while($hs{$recf}) 
            {
		        $recf = '';
                $data = '';
                $mask = '';
                $mask_num = int(rand(129));
                for(my $par=0; $par<128; $par++) 
                {
                    $ip_sec = int(rand(2));
                    $data = $data.$ip_sec;
                    
                    if($par < (128-$mask_num)) 
                    {
                        $recf = $recf.$ip_sec;
                        $mask = $mask."0";
                    }
                    else 
                    {
                        $recf = $recf."0"; 
                        $mask = $mask."1";
                    }
                }
				#print "identical and repeat\n";
			}
		 }
		else
		{
			$data= "";
			$mask= "";
            $recf = $recf.sprintf("%08b",(($rec_num>>24)&0xFF));
            $recf = $recf.sprintf("%08b",(($rec_num>>16)&0xFF));  
            $recf = $recf.sprintf("%08b",(($rec_num>>8)&0xFF));  
            $recf = $recf.sprintf("%08b",(($rec_num)&0xFF));
              for(my $par = 0; $par < 96; $par++)
            {
                $recf=$recf.'0';
            }            
             for(my $par = 0; $par < 128; $par++)
            {
                $mask=$mask.'0';
            }   
			
			$data = $recf;
		}
            $hs{$recf}++;
            for(my $par = 0; $par < 16; $par++)
            {
                $ip_sec = int(rand(2));
                $data = $data.$ip_sec;
                $mask = $mask."1";
            }
			if($type eq 0)
            {
                print $fp_ip $data.",$mask";
            }
            else
            {
                print $fp_ip $recf;
            }
			
         }

         
        else 
        {
           if($inc eq 0)
          { 
            for(my $par=0; $par<144; $par++) 
            {
                $ip_sec = int(rand(2));
                $data = $data.$ip_sec;
                if($par < (144-$mask_num)) 
                {
                    $recf = $recf.$ip_sec;
                    $mask = $mask."0";
                } else 
                {
                    $recf = $recf."0";
                    $mask = $mask."1";
                }
            }
			
			# If the rule is already exist, recalculate a new one
            while($hs{$recf}) 
            {
		        $recf = '';
                $data = '';
                $mask = '';
                $mask_num = int(rand(129));
            
                for(my $par=0; $par<144; $par++) 
                {
                    $ip_sec = int(rand(2));
                    $data = $data.$ip_sec;
                    if($par < (144-$mask_num)) 
                    {
                        $recf = $recf.$ip_sec;
                        $mask = $mask."0";
                    }
                    else 
                    {
                        $recf = $recf."0"; 
                        $mask = $mask."1";
                    }
                }
				#print "identical and repeat\n";
			}
		 }
		 else
         {
            $recf= "";
            $recf = $recf.sprintf("%08b",(0&0xFF));
            $recf = $recf.sprintf("%08b",(0&0xFF)); 
            $recf = $recf.sprintf("%08b",(($rec_num>>24)&0xFF));
            $recf = $recf.sprintf("%08b",(($rec_num>>16)&0xFF));  
            $recf = $recf.sprintf("%08b",(($rec_num>>8)&0xFF));  
            $recf = $recf.sprintf("%08b",(($rec_num)&0xFF));
              for(my $par = 0; $par < 96; $par++)
            {
                $recf=$recf.'0';
            }  
			for(my $par = 0; $par < 144; $par++)
            {
                $mask=$mask.'0';
            }          
			
            $data = $recf;
         }
            $hs{$recf}++;
			if($type eq 0)
            {
                print $fp_ip $data.",$mask";
            }
            else
            {
                print $fp_ip $recf;
            
            }
            
           
        
        }
        printf $fp_ip ("\,%d\n", $rec_num);
    }
} # rec_num


print "---------------------- END of Production --------------------------------------------\n";
