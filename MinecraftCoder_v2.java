/**
 * @(#)MinecraftCoder_v2.java
 *
 *
 * @author
 * @version 1.00 2015/3/11
 */
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

public class MinecraftCoder_v2
{
    /* Outdated method
    public static void compile(String filename)
    {
    	try
    	{
	    	LinkedList<String> commands = new LinkedList<String>();
	    	LinkedList<String> inputs;
	    	String line;
	    	BufferedReader reader = new BufferedReader(new FileReader(new File(filename + ".txt")));

			while((line=reader.readLine())!=null)
			{
				if(!line.substring(0,1).equals(";"))
				{
					commands.push(line);
				}

			}

			reader.close();

			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename + "_compiled_v2.txt")));

			writer.write("/summon FallingSand ~ ~2 ~ {Block:redstone_block,Time:1,Riding:\r\n");

			writer.write("{id:FallingSand,Block:command_block,TileEntityData:\r\n");
			writer.write("\t{Command:" + addCommands(commands,1) + "},Time:1}}\r\n");

			writer.flush();
			writer.close();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }*/

    public static File compile(File file,EditorOverlord output)
    {
    	try
    	{
	    	LinkedList<String> commands = new LinkedList<String>();
	    	LinkedList<String> inputs;
	    	String line;
	    	BufferedReader reader = new BufferedReader(new FileReader(file));
	    	int charCount = 0,xOffset = 0,yOffset = 0,zOffset = 0;

			while((line=reader.readLine())!=null)
			{
				if(!line.substring(0,1).equals(";"))
				{
					if(line.substring(0,1).equals("@"))
					{
						switch (line.charAt(1))
						{
							case 'x':
								xOffset = Integer.parseInt(line.substring(2).trim());
								break;
							case 'y':
								yOffset = Integer.parseInt(line.substring(2).trim());
								break;
							case 'z':
								zOffset = Integer.parseInt(line.substring(2).trim());
								break;
						}
					}
					else
					{
						commands.push(line);
					}
				}
			}

			reader.close();

			String filePath = file.getPath();
			filePath = filePath.substring(0,filePath.length()-5) + "_compiled.txt";
			File compiledFile = new File(filePath);

			BufferedWriter writer = new BufferedWriter(new FileWriter(compiledFile));

			writer.write("/summon FallingSand ~ ~2 ~ {Block:redstone_block,Time:1,Riding:\r\n");
			charCount += 63;

			writer.write("{id:FallingSand,Block:command_block,TileEntityData:\r\n");
			charCount += 51;
			String fillCommands = addCommands(commands,1,xOffset,yOffset,zOffset,0);
			writer.write("\t{Command:" + fillCommands + "},Time:1}}\r\n");
			charCount += fillCommands.length() + 19;

			output.addToOutput("Compiled file " + file.getName());
			output.addToOutput("Length of complied file: " + charCount + "/32767");

			writer.flush();
			writer.close();

			return compiledFile;
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}

    	return null;
    }

    public static String addCommands(LinkedList<String> commands,int stacks,int xOffset,int yOffset,int zOffset,int summonOffset)
    {
    	String writer = "";
		LinkedList<String> inputs = new LinkedList<String>();
		int brackets = 1;
		int stackHeight = 60;

		for(int i=0;i<stackHeight*2.0/3.0;i++)
		{
			if(commands.size()>0)
				inputs.push(commands.pollLast());
			else
				break;
		}

		if(commands.size()!=0)
		{
			inputs.push(addCommands(commands,stacks+1,xOffset,yOffset,zOffset,inputs.size()+1));
		}
		else
		{
			inputs.push("/summon FallingSand ~-1 ~ ~ {Block:redstone_block,Time:1,Riding:{id:FallingSand,Block:command_block,TileEntityData:{Command:fill ~" + (stacks+1) + " ~0 ~0 ~0 ~" + (stackHeight+1) + " ~0 air},Time:1}}");
		}

		writer += (getCommand(inputs,xOffset+stacks,yOffset,zOffset,summonOffset));

		return writer;
    }

    public static String getCommand(LinkedList<String> commands,int xOffset,int yOffset,int zOffset,int summonOffset)
    {
    	int brackets = (int)Math.floor(commands.size()*3/2.0)+1;
		int yCordFix = -brackets+1+yOffset;
		String writer = "",line;

		writer += ("/summon FallingSand ~-1 ~" + (2-summonOffset) + " ~" + zOffset + " {Block:redstone_block,Time:1,Riding:\r\n");
		while(commands.size()>=2)
		{
			line = fixCords(commands.pop(),xOffset,yCordFix,zOffset);
			writer += ("{id:FallingSand,Block:command_block,TileEntityData:\r\n");
			writer += ("\t{Command:" + line + "},\r\n");
			writer += ("\tTime:1,Riding:\r\n");
			yCordFix++;

			line = fixCords(commands.pop(),xOffset,yCordFix,zOffset);
			writer += ("{id:FallingSand,Block:command_block,TileEntityData:\r\n");
			writer += ("\t{Command:" + line + "},\r\n");
			writer += ("\tTime:1,Riding:\r\n");
			yCordFix++;

			writer += ("{id:FallingSand,Block:redstone_block,Time:1,Riding:\r\n");
			yCordFix++;
		}
		if(commands.size()==1)
		{
			line = fixCords(commands.pop(),xOffset,yCordFix,zOffset);
			writer += ("{id:FallingSand,Block:command_block,TileEntityData:\r\n");
			writer += ("\t{Command:" + line + "},\r\n");
			writer += ("\tTime:1\r\n");
			yCordFix++;
		}
		for(int i=0;i<brackets;i++)
		{
			writer += ("}");
		}

		return writer;
    }

    public static String fixCords(String command,int xOffset,int yOffset,int zOffset)
    {
    	try
    	{
	    	int startXCord = command.indexOf('~');
		    int startYCord,startZCord,endZCord;
		    double xCord,yCord,zCord;

		    while(startXCord!=-1 && startXCord<50 && !command.substring(0,19).equals("/summon FallingSand"))
		    {
	    		startYCord = command.indexOf('~',startXCord+1);

	    		xCord = Double.parseDouble(command.substring(startXCord+1,startYCord-1));
	    		xCord += xOffset;

	    		command = command.substring(0,startXCord+1)+xCord+command.substring(startYCord-1);

	    		startYCord = command.indexOf('~',startXCord+1);
	    		startZCord = command.indexOf('~',startYCord+1);

	    		yCord = Double.parseDouble(command.substring(startYCord+1,startZCord-1));
	    		yCord += yOffset;

	    		command = command.substring(0,startYCord+1)+yCord+command.substring(startZCord-1);

	    		startZCord = command.indexOf('~',startYCord+1);
	    		endZCord = command.indexOf(' ',startZCord);

	    		zCord = Double.parseDouble(command.substring(startZCord+1,endZCord));
	    		zCord += zOffset;

	    		command = command.substring(0,startZCord+1)+zCord+command.substring(endZCord);

	    		startXCord = command.indexOf('~',endZCord);
	    	}

	    	return command;
    	}
    	catch(Exception ex)
    	{
    		System.out.println(command);
    		ex.printStackTrace();

    		return "";
    	}
    }

    public static void main(String[] args)
    {
    	//compile(args[0]);
    }
}

