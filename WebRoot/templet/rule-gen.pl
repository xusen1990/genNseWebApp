#!/usr/bin/env perl

use strict;
use warnings;

sub helpInfo
{
    print "Usage: $0 [-h] [-s <seed>] [-m <mode>] [-w <width>] [-d <depth>] [-f <s1,s2,s3,...>]\n\n";
    print "  -s <seed>   : specific random seed\n";
    print "  -w <width>  : specific table width\n";
    print "                   [80/160/240/320/400/480/560/640 bits, 160 by default]\n";
    print "  -d <depth>  : specific table depth\n";
    print "                   [unit K=1024, 1K by default]\n";
    print "  -m <mode>   : specific print output format \n";
    print "                   [0 -> human-readable by default] \n";
    print "                   [1 -> hex string] \n";
    print "                   [2 -> pure binary] \n";
    print "                   [3 -> binary] \n";
    print "  -f <segs>   : specific rule generation format \n";
    print "                   [avaiable segments splited by comma: s1,s2,s3,s4,s5] \n";
    print "                   [   s1 delegates IP adress V4, 4 bytes taken] \n";
    print "                   [   s2 delegates IP adress V6, 16 bytes taken] \n";
    print "                   [   s3 delegates MAC adress, 6 bytes taken] \n";
    print "                   [   s4 delegates port number, 2 bytes taken] \n";
    print "                   [   s5 delegates protocol number, 1 byte taken] \n";
    print "                   [default format: -f s3,s3,s1,s1 if there's no -f option  (160 bits)] \n";
    print "  -t          : generate table \n";
	print "  -i          : inc mode \n";
    print "  -h          : print this help info \n";
	print "  -p 		 : print path \n";
    print "\nOutput format (human-readable):\n";
    print "    ###############################\n";
    print "    |generated rule content|,|uuid|\n";
    print "    |generated mask content|\n";
    print "    ###############################\n";
    print "\nExample rule format:\n";
    print "    80 bits: [-f s3,s1] means <MAC-6B>,<IPv4-4B>,<UUID-4B>\n";
    print "   160 bits: [-f s3,s3,s1,s1] means <MAC-6B>,<MAC-6B>,<IPv4-4B>,<IPv4-4B>,<UUID-4B>\n";
    print "   320 bits: [-f s3,s2,s2,s4] means <MAC-6B>,<IPv6-16B>,<IPv6-16B>,<PORT-2B>,<UUID-4B>\n";
    print "   640 bits: [-f s3,s3,s2,s2,s2,s2,s4,s4] means <MAC-6B>,<MAC-6B>,<IPv6-16B>,<IPv6-16B>,<IPv6-16B>,<IPv6-16B>,<PORT-2B>,<PORT-2B>,<UUID-4B>\n\n";
    exit 0;
}

my $randomSeed = undef;
my $outputMode = 0;
my $tableWidth = 160;
my $tableDepth = 1024;
my $Tname = "T.txt";
my $incrule = 0 ;
my @ruleFormat = ("s3", "s3", "s1", "s1");
my $path = "C:\\Users\\peng.zhang\\Desktop\\work\\genNseApp\\templet\\";

while (@ARGV) 
{
    my $arg = shift @ARGV;
    if ($arg =~ /-w/)
    {
        my $width = shift @ARGV;
        if ($width !~ /^80$|^160$|^240$|^320$|^400$|^480$|^560$|^640$/)
        {
            &helpInfo;
        }
        $tableWidth = $width;
    }
    elsif ($arg =~ /-s/)
    {
        my $seed = shift @ARGV;
        if ($seed !~ /^\d+$/)
        {
            &helpInfo;
        }
        $randomSeed = $seed;
        srand($seed);
    }
    elsif ($arg =~ /-d/)
    {
        my $depth = shift @ARGV;
        if ($depth !~ /^\d+$/ or $depth == 0)
        {
            &helpInfo;
        }
        $tableDepth *= $depth;
    }
    elsif ($arg =~ /-m/)
    {
        my $mode = shift @ARGV;
        if ($mode !~ /^[0123]$/)
        {
            &helpInfo;
        }
        $outputMode = $mode;
    }
    elsif ($arg =~ /-f/)
    {
        my $format = shift @ARGV;
        if($format !~ /^s[12345](,s[12345]){0,},?$/ )
        {
            &helpInfo;
        }
        @ruleFormat = split(/,/, $format);
    }
    elsif ($arg =~ /-t/)
    {
        my $T = shift @ARGV;
        $Tname = $T.".txt";
    }
	elsif ($arg =~ /-i/)
    {
        my $inc = shift @ARGV;
        if ($inc !~ /^[01]$/)
        {
            &helpInfo;
        }
        $incrule = $inc;
    }
	elsif ($arg =~ /-p/)
    {
        my $ipath = shift @ARGV;
        $path = $ipath;
    }
    else
    {
        &helpInfo;
    }
}

my $randIdMax = $tableDepth;
my %uuidHash = (); 
sub genUuid
{
    my $uuid = undef;
    while(1) {
        $uuid = int(rand($randIdMax));
        next if (exists $uuidHash{$uuid});
        $uuidHash{$uuid} = 1;
        last;
    }
    return $uuid;
}

sub genMacAddr
{
    my $mode = $_[0];
    my @mac = ("00") x 6;
    my @mac_1 = undef;
    my @macOutput = undef;
    for my $i (0..5) { 
        my $temp = int(rand(256));
        $mac[$i] = sprintf("%02x", $temp);
        $mac_1[$i] = sprintf("%08b", $temp);
    }
    if($mode == 0) {
        $macOutput[0] = join(":", @mac).",";
        $macOutput[1] = "00:00:00:00:00:00,";
    } elsif ($mode == 1) {
        $macOutput[0] = join("", @mac);
        $macOutput[1] = '0' x 12;
    } elsif ($mode == 2) {
        $macOutput[0] = pack("H12", join("", @mac));
        $macOutput[1] = pack("H12", '0' x 12);
    } elsif ($mode == 3) {
        $macOutput[0] = join("", @mac_1);
        #print("3: $macOutput[0]\n");
        $macOutput[1] = '0' x 48;
        #print("4: $macOutput[1]\n");
    }else {
        warn "Error call" && exit 1;
    }
    return @macOutput;
}

sub genIpv4Addr
{
    my $mode = $_[0];
    my @ipv4 = (0) x 4;
    my @mask = (255) x 4;
    my @ipv4Output = undef;
    do { 
        $ipv4[0] = int rand(240); 
    } while($ipv4[0] == 0 || $ipv4[0] == 127);
    for my $i (1..3) { 
        $ipv4[$i] = int(rand(255));
    }
    my $vlsm = undef;
    do {
        $vlsm = int rand(33);
    } while($vlsm < 8);
    my $bitMask = '1' x $vlsm.'0' x (32-$vlsm);
    for my $i (0..3) {
        $mask[$i] = 255 - oct("0b".substr($bitMask, $i*8, 8));
    }
    if($mode == 0) {
        $ipv4Output[0] = join(".", @ipv4).",";
        $ipv4Output[1] = join(".", @mask).",";
    } elsif ($mode == 1) {
        for my $i (0..3) {
            $ipv4[$i] = sprintf("%02x", $ipv4[$i]);
            $mask[$i] = sprintf("%02x", $mask[$i]);
        }
        $ipv4Output[0] = join("", @ipv4);
        $ipv4Output[1] = join("", @mask);
    } elsif ($mode == 2) {
        for my $i (0..3) {
            $ipv4[$i] = sprintf("%02x", $ipv4[$i]);
            $mask[$i] = sprintf("%02x", $mask[$i]);
        }
        $ipv4Output[0] = pack("H8", join("", @ipv4));
        $ipv4Output[1] = pack("H8", join("", @mask));
    } elsif ($mode == 3) {
       for my $i (0..3) {
            $ipv4[$i] = sprintf("%08b", $ipv4[$i]);
            $mask[$i] = sprintf("%08b", $mask[$i]);
        }
        $ipv4Output[0] = join("", @ipv4);
        #print("1: $ipv4Output[0]\n");
        $ipv4Output[1] = join("", @mask);
        #print("2: $ipv4Output[1]\n");
    } else {
        warn "Error call" && exit 1;
    }
    return @ipv4Output;
}

sub genIpv6Addr
{
    my $mode = $_[0];
    my @ipv6 = (0) x 8;
    my @ipv6_1 = (0) x 32;
    my @ipv6Output = undef;
    do { 
        $ipv6[0] = sprintf("%04x", int(rand(65536)));
    } while($ipv6[0] =~ m/fe80/i or $ipv6[0] =~ m/fd../i);
    for my $i (1..7) { 
        $ipv6[$i] = sprintf("%04x", int(rand(65536))); 
    }
    if($mode == 0) {
        $ipv6Output[0] = join(":", @ipv6).",";
        $ipv6Output[1] = "0000:"x4 . "ffff:"x3 . "ffff,";
    } elsif ($mode == 1) {
        $ipv6Output[0] = join("", @ipv6);
        $ipv6Output[1] = "0000"x4 . "ffff"x4;
    } elsif ($mode == 2) {
        $ipv6Output[0] = pack("H32", join("", @ipv6));
        $ipv6Output[1] = pack("H32", "0000"x4 . "ffff"x4);
    } elsif ($mode == 3) {
        $ipv6Output[0] = join("", @ipv6_1);
        $ipv6Output[1] = "0000"x16 . "1111"x16;
    } else {
        warn "Error call" && exit 1;
    }
    return @ipv6Output;
}

sub genPortNum
{
    my $mode = $_[0];
    my @portOutput = undef;
    my $temp = int(rand(65536));
    my $port = sprintf("%04x", $temp);
    my $port_1 = sprintf("%016b", $temp);
    if($mode == 0) {
        $portOutput[0] = oct("0x".$port).",";
        $portOutput[1] = "0,";
    } elsif ($mode == 1) {
        $portOutput[0] = $port;
        $portOutput[1] = "0000";
    } elsif ($mode == 2) {
        $portOutput[0] = pack("H4", $port);
        $portOutput[1] = pack("H4", '0' x 4);
    } elsif ($mode == 3) {
        $portOutput[0] = $port_1;
        $portOutput[1] = "0000" x 4;
    } else {
        warn "Error call" && exit 1;
    }
    return @portOutput;
}

sub genProtocol
{
    my $mode = $_[0];
    my $proto = 0;
    my $proto_1 = 0;
    my @protoOutput = undef;
    $proto = sprintf("%02x", int(rand(256)));
    $proto_1 = sprintf("%08b", int(rand(256)));
    if($mode == 0) {
        $protoOutput[0] = oct("0x".$proto).",";
        $protoOutput[1] = "0,";
    } elsif ($mode == 1) {
        $protoOutput[0] = $proto;
        $protoOutput[1] = "00";
    } elsif ($mode == 2) {
        $protoOutput[0] = pack("H2", $proto);
        $protoOutput[1] = pack("H2", '0' x 2);
    } elsif ($mode == 3) {
        $protoOutput[0] = $proto;
        $protoOutput[1] = "00" x 4;
    } else {
        warn "Error call" && exit 1;
    }
    return @protoOutput;
}

sub uuidStr2Hex
{
    return sprintf("%08x", $_[0]);
}

sub uuidStr2Bin
{
    return pack("L1", $_[0]);
}

sub uuidStrbin
{
  
    return sprintf("%b", $_[0]);
 
}
sub fillOutput
{
    my ($ruleRef, $maskRef, $bits, $mode) = @_;
    if($mode != 3 )
    {     
        $bits /= 4;
    }
    if ($mode == 1) {
        $$ruleRef = '0' x $bits . $$ruleRef;
        $$maskRef = '0' x $bits . $$maskRef;
    } elsif ($mode == 2) {
        $$ruleRef = pack("H".$bits, '0' x $bits) . $$ruleRef;
        $$maskRef = pack("H".$bits, '0' x $bits) . $$maskRef;
    }elsif($mode == 3) {
        $$ruleRef = '0' x $bits . $$ruleRef;
        $$maskRef = '0' x $bits . $$maskRef;
    }
}

my %segInByte = ( "s1" => 4,    
                  "s2" => 16,   
                  "s3" => 6,    
                  "s4" => 2,    
                  "s5" => 1,    
);
my %segGenFunc = ( "s1" => \&genIpv4Addr,   
                   "s2" => \&genIpv6Addr,  
                   "s3" => \&genMacAddr,   
                   "s4" => \&genPortNum,   
                   "s5" => \&genProtocol,   
);

my $totalBits = 0; 
foreach (@ruleFormat) {
    $totalBits += $segInByte{$_}*8;
}
if($totalBits > $tableWidth)
{
    print "Error usage:\n";
    print "    Format [".join(",",@ruleFormat)."] takes $totalBits bits,";
    print " but configed table width is $tableWidth bits.\n";
    print "    Use -w option to set right width bigger than $totalBits.\n";
    exit 1;
}

my $lines = 0;
my %ruleHash = ();

open(TNAME,">$path$Tname") or die "can't open file: $Tname\n"; #write
while($lines < $tableDepth)
{
  
    my $rule = undef;
    my $mask = undef;
    my $uuid = undef;
	if($incrule == 0)
   {
    foreach (@ruleFormat) {
        my ($e1, $e2)= $segGenFunc{$_}($outputMode);
        $rule .= $e1; $mask .= $e2;
    }
    if($totalBits != $tableWidth)
    {
        &fillOutput(\$rule, \$mask, $tableWidth - $totalBits, $outputMode);
    }
    # make sure no duplication rule
    next if (exists $ruleHash{$rule});
    $ruleHash{$rule} = 1;
    $uuid = &genUuid;
    if ($outputMode == 0)
    {
      print TNAME $rule.$uuid."\n";
      print TNAME $mask."X\n";
    }

    if ($outputMode == 1)
    {
     print TNAME $rule.&uuidStr2Hex($uuid)."\n" ;
     print TNAME $mask."\n"
    }
    
   if($outputMode == 2)
   {
    print TNAME $rule.&uuidStr2Bin($uuid);
    print TNAME $mask; 
    }
     if ($outputMode == 3)
    {
        print TNAME $rule.",$mask,$uuid\n";
    }
   }
   else
   {
			$rule = "";
			$mask = "";
            $rule = $rule.sprintf("%08b",(0&0xFF));
            $rule = $rule.sprintf("%08b",(0&0xFF));
            $rule = $rule.sprintf("%08b",(($lines>>24)&0xFF));
            $rule = $rule.sprintf("%08b",(($lines>>16)&0xFF));  
            $rule = $rule.sprintf("%08b",(($lines>>8)&0xFF));  
            $rule = $rule.sprintf("%08b",(($lines)&0xFF)); 
			
			$mask = $mask.sprintf("%08b",(0&0xFF));
            $mask = $mask.sprintf("%08b",(0&0xFF));  
            $mask = $mask.sprintf("%08b",(0&0xFF));  
            $mask = $mask.sprintf("%08b",(0&0xFF)); 
			$mask = $mask.sprintf("%08b",(0&0xFF));  
            $mask = $mask.sprintf("%08b",(0&0xFF));	
			for(my $i = 48 ; $i < $tableWidth ; $i++ )
			{
			   $rule=$rule.'0';
			   $mask=$mask.'0';
			}
			
			print TNAME $rule.",$mask,$lines\n";
   }
    $lines++;
}
