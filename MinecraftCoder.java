/**
 * @(#)MinecraftCoder.java
 *
 * MinecraftCoder application
 *
 * @author
 * @version 1.00 2015/2/28
 */
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

public class MinecraftCoder
{
    public static void compile(String filename)
    {
    	try
    	{
	    	LinkedList<String> commands = new LinkedList<String>();
	    	String line;
	    	BufferedReader reader = new BufferedReader(new FileReader(new File(filename + ".txt")));

			while((line=reader.readLine())!=null)
			{
				if(!line.substring(0,1).equals(";"))
					commands.push(line);
			}

			reader.close();

			int brackets = (int)Math.floor(commands.size()*3/2.0)+1;
			int yCordFix = brackets-1;

			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename + "_compiled.txt")));

			writer.write("/summon FallingSand ~ ~2 ~ {Block:redstone_block,Time:1,Riding:\r\n");
			while(commands.size()>=2)
			{
				line = fixYCords(commands.pop(),yCordFix);
				writer.write("{id:FallingSand,Block:command_block,TileEntityData:\r\n");
				writer.write("\t{Command:" + line + "},\r\n");
				writer.write("\tTime:1,Riding:\r\n");
				yCordFix--;

				line = fixYCords(commands.pop(),yCordFix);
				writer.write("{id:FallingSand,Block:command_block,TileEntityData:\r\n");
				writer.write("\t{Command:" + line + "},\r\n");
				writer.write("\tTime:1,Riding:\r\n");
				yCordFix--;

				writer.write("{id:FallingSand,Block:redstone_block,Time:1,Riding:\r\n");
				yCordFix--;
			}
			if(commands.size()==1)
			{
				line = fixYCords(commands.pop(),yCordFix);
				writer.write("{id:FallingSand,Block:command_block,TileEntityData:\r\n");
				writer.write("\t{Command:" + line + "},\r\n");
				writer.write("\tTime:1\r\n");
				yCordFix--;
			}
			for(int i=0;i<brackets;i++)
			{
				writer.write("}");
			}

			writer.flush();
			writer.close();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }

    public static String fixYCords(String command,int offset)
    {
	    int startXCord = command.indexOf('~');
	    int startYCord,endYCord,startZCord;
	    double yCord;

	    while(startXCord!=-1)
	    {
    		startYCord = command.indexOf('~',startXCord+1);
    		endYCord = command.indexOf(' ',startYCord);

    		yCord = Double.parseDouble(command.substring(startYCord+1,endYCord));
    		yCord -= offset;

    		command = command.substring(0,startYCord+1)+yCord+command.substring(endYCord);

    		startZCord = command.indexOf('~',endYCord);
    		startXCord = command.indexOf('~',startZCord+1);
    	}

    	return command;

    }

    public static void main(String[] args)
    {
    	compile(args[0]);
    }
}
