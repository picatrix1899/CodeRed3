package com.codered;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;


public class ArgumentInterpreter
{
		private HashMap<String, Argument> interpreter = new HashMap<>();
		
		public void addArgument(String args, Argument destination)
		{
			this.interpreter.put(args.toLowerCase(), destination);
		}
		
		public void interpret(String[] args)
		{
			int index = 0;
			
			String currentArg = "";
			String[] values = new String[0];
			
			while(index < args.length)
			{
				if(args[index].startsWith("-"))
				{	
					currentArg = args[index].substring(1, args[index].length()).toLowerCase();
					
					if(this.interpreter.containsKey(currentArg))
					{
						for(int i = index + 1; i < args.length; i++)
						{
							if(args[i].startsWith("-"))
							{
								if(i > index + 1)
								{
									values = Arrays.copyOfRange(args, index + 1, i);
									index = i - 1;
								}
								else
								{
									values = new String[0];
								}
								break;
							}
							else if(i == args.length - 1)
							{
								values = Arrays.copyOfRange(args, index + 1, i + 1);
								index = i;
							}
						}
						
						this.interpreter.get(currentArg).accept(values);
						values = new String[0];
					}
				}
				
				index++;
			}
		}
		
	public interface Argument extends Consumer<String[]> { }
}
