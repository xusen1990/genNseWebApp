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
my $type      = 0;       #type rule/search
my $dest      = "";      #gen txt
my $source    = "";       #need txt
my $path      ="C:\\Users\\peng.zhang\\Downloads\\apache-tomcat-8.0.48\\webapps\\genNseWebApp\\templet\\";        #path
my $head       = "";
my $delete    = 0 ;


use constant USAGE => <<'HERE';
    -----------------------------------------------------------------------------
    # REVISION LOG
    #-----------------------------------------------------------------------------
    2018/01/24     V0.1      Initial Version  muzp
    -----------------------------------------------------------------------------
    # Usage
    -----------------------------------------------------------------------------
    OPTION          |DEFAULT  |DESCRIPTION    -h|help          off       Print help information. <CMD>
    -h|help          off       Print help information. <CMD>
    -dest            dest      generate the file we need
    -source          source    the data we need
    -type            0/1       rule/search
    -path            path      path
    -delete          0/1       save/delete
    -----------------------------------------------------------------------------
    # EXAMPLES
    -----------------------------------------------------------------------------

HERE

#################################################################################
## GetOption
#################################################################################
GetOptions(
    "h|help"       => \$help,
    "type=i"           => \$type,
    "dest=s"          => \$dest,
    "source=s"     => \$source,
    "path=s"     => \$path,
    "delete=i"     => \$delete,
);
die USAGE if $help;

#################################################################################
## Parse Options
#################################################################################
print "Produce ".($type ? "search " : "type ")."records "."\n";
my @sourcefile;
my $destfile;
@sourcefile = split(/,/, $source);
$destfile = $dest.".txt";
my $start = time;
open(DEST,">$path$destfile") or die "can't open file: $destfile\n"; #write
if($type == 0)
{
    foreach (@sourcefile)
    {
        $head = ($_).".txt";
        print DEST "#".$head."\n";
        open(FP , "<$path$head")or die "can't open file: $head\n"; #write
        while(<FP>){

            print DEST $_;

        }
        close(FP);
        if($delete)
        {
            unlink "$path$head";
        }

    }
    close(DEST);
}
else
{
    my @search;
    for(my $pair = 0 ; $pair<1024 ; $pair++)
    {
        $search[$pair] = "";
    }
    my $num;
    foreach (@sourcefile)
    {
        $head = ($_).".txt";

        open(FP,"<$path$head")or die "can't open file: $head\n"; #write
        $num = 0;



        while(<FP>){
            my @temp = split(',',$_);
            $search[$num] = $search[$num].$temp[0];
            if($num >= 1023)
            {
                last;
            }
            $num += 1;
        }
        close(FP);
    }

    foreach(@search)
    {
        print DEST $_."\n";
    }
    close(DEST);
}

my $duration = time - $start;

print "while loop Execution time: $duration s\n";


print "---------------------- END of Production --------------------------------------------\n";
