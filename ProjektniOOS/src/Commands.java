
import java.io.*;
import java.lang.reflect.Method;
import java.util.Scanner;

public class Commands
{
    private static int counter=0;

    public static void input()
    {
        try(Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String command = scanner.nextLine();
                String methodName = command.split(" ")[0];
                try {
                    Class<?> c = Class.forName("Commands");
                    Method method = c.getMethod(methodName, String.class);
                    method.invoke(null, command);
                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("Command doesn't exist.");
                }

            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static void errorMessage(String message)
    {
        System.out.println("Wrong use of command " + message + ". Too many arguments. Try again.");
    }


    public static void where(String arguments)
    {
        if(arguments.split(" ").length>1)
        {
            errorMessage(arguments.split(" ")[0]);
            return;
        }
        System.out.println(System.getProperty("user.dir"));
    }

    public static void go(String arguments) throws Exception
    {
        String[] argArray = arguments.split(" ");
        if(argArray.length>2)
        {
            errorMessage(argArray[0]);
            return;
        }
        else if(argArray.length<2)
        {
            System.out.println("This command requires arguments");
            return;
        }
        File file;
            if(argArray[1].contains(File.separator))
                file = new File(argArray[1]);
            else
                file = new File(System.getProperty("user.dir") + File.separator + argArray[1]);
            if(file.exists() && file.isDirectory())
                    System.setProperty("user.dir", file.getCanonicalPath());
            else
            {
                System.out.println("Bad path or directory doesn't exist.");
                return;
            }

    }


    public static void create(String arguments){
        String[] argArray = arguments.split(" ");
        try {
            if (argArray.length > 3) {
                errorMessage(argArray[0]);
                return;
            }
            if(argArray.length<2)
            {
                System.out.println("This command requires arguments.");
                return;
            }
            File file;
            if (argArray[1].equals("-d") && argArray.length>2) {
                if (argArray[2].contains(File.separator))
                    file = new File(argArray[2]);
                else
                    file = new File(System.getProperty("user.dir") + File.separator + argArray[2]);
                if(file.exists() && file.isDirectory())
                    throw new Exception();
                file.mkdirs();

            }
            else if(argArray[1].equals("-d"))
            {
                System.out.println("Wrong usage of command create. A path needs to be provided.");
                return;
            }
            else {
                if(argArray.length>2)
                {
                    errorMessage(argArray[0]);
                    return;
                }
                if (argArray[1].contains(File.separator))
                    file = new File(argArray[1]);
                else
                    file = new File(System.getProperty("user.dir") + File.separator + argArray[1]);

                if(file.exists())
                    throw new Exception();
                file.createNewFile();
            }


        }
        catch (Exception e)
        {
            //System.out.println("File or directory already exists. Try again");
            System.out.println("Error. Try again.");
        }
    }

    public static void list(String arguments) {
        String[] argArray = arguments.split(" ");
        if (argArray.length > 2)
        {
            errorMessage(argArray[0]);
            return;
        }
        try
        {
            if (argArray.length == 1)
            {
                File file = new File(System.getProperty("user.dir"));
                System.out.println(file.getName());
                recursiveList(file, 0);
            }
            else
            {
                File dir = new File(argArray[1]);
                if(dir.exists() && dir.isDirectory())
                    recursiveList(dir,0);
                else
                    throw new Exception();
            }

        }
        catch (Exception e)
        {
            System.out.println("Directory doesn't exist.");
        }
    }

    public static void printTabs(int depth)
    {
        for(int i=0;i<depth;i++)
            System.out.print('\t');
    }

    public static void recursiveList(File dir, int depth)
    {
        //printTabs(depth);
        //System.out.println(dir.getName());
        File[] files = dir.listFiles();
        for(File file : files)
        {
            if(file.isDirectory()) {
                printTabs(depth+1);
                System.out.println(file.getName());
                recursiveList(file, depth+1);
            }
            else
            {
                printTabs(depth+1);
                System.out.println(file.getName());
            }
        }

    }

    public static void print(String arguments) throws IOException {
        String[] argArray = arguments.split(" ");
        File file;
        if(argArray.length>2)
        {
            errorMessage(argArray[0]);
            return;
        }
        if(argArray.length<2)
        {
            System.out.println("This commands requires arguments.");
            return;
        }
        if(argArray[1].contains(File.separator))
            file = new File(argArray[1]);
        else
            file = new File(System.getProperty("user.dir") + File.separator + argArray[1]);

        if(file.exists() && file.getName().endsWith(".txt") && !file.isDirectory())
        {
            String str;
            try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                while ((str = br.readLine()) != null) {
                    System.out.println(str);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
            System.out.println("Wrong file format or file doesn't exist. Try again.");
    }

    public static void find(String arguments) throws IOException {
        String[] argArray = arguments.split(" ");
        String find;
        if(argArray.length > 3)
        {
            errorMessage(argArray[0]);
            return;
        }
        if(argArray.length<=2)
        {
            System.out.println("This command requires arguments.");
            return;
        }
        //char[] findSequence = argArray[1].toCharArray();
        if(argArray[1].startsWith("\"") && argArray[1].endsWith("\""))
            find = argArray[1].substring(1, argArray[1].length()-1);
        else
        {
            System.out.println("Second argument needs to be between quotation marks.");
            return;
        }
        File file;
        if(argArray[2].contains(File.separator))
            file = new File(argArray[2]);
        else
            file = new File(System.getProperty("user.dir") + File.separator + argArray[2]);
        if(file.exists() && !file.isDirectory())
        {
            try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                String str;
                int row = 0;
                while ((str = br.readLine()) != null) {
                    row++;
                    if (str.contains(find)) {
                        System.out.println(find + " is in line " + row + " of file " + file.getName());
                        return;
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("File doesn't exist or is a directory.");
            return;
        }
        System.out.println(file.getName() + " doesn't contain " + find);
    }

    public static void recursiveSearch(File dir, String filename) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                recursiveSearch(file, filename);
            } else {
                if (file.getName().equals(filename)) {
                    System.out.println("File " + filename + " is found in directory " + dir.getAbsolutePath());
                    counter++;
                }

            }
        }
    }


    public static void findDat(String argument)
    {
        String[] argArray= argument.split(" ");
        if(argArray.length>3)
        {
            errorMessage(argArray[0]);
            return;
        }
        if(argArray.length<=2)
        {
            System.out.println("This command requires arguments.");
            return;
        }
        File file;
        if(argArray[2].contains(File.separator))
            file = new File(argArray[2]);
        else
            file = new File(System.getProperty("user.dir") + File.separator + argArray[2]);
        if(file.exists() && file.isDirectory())
            recursiveSearch(file, argArray[1]);
        else
        {
            System.out.println("Directory doesn't exist or isn't a directory");
            return;
        }
        if(counter==0)
        {
            System.out.println("File doesn't exist in the specified directory.");
            return;
        }
        counter=0;
    }

    public static void logout(String argument)
    {
        String[] argArray = argument.split(" ");
        if(argArray.length>1)
        {
            errorMessage(argArray[0]);
            return;
        }
        System.exit(1);
    }

    public static void back(String argument)
    {
        String[] argArray = argument.split(" ");
        if(argArray.length>1)
        {
            errorMessage(argArray[0]);
            return;
        }
        //String currDir = System.getProperty("user.dir");
        //String newDir = currDir.substring(0, currDir.lastIndexOf('\\'));
        System.setProperty("user.dir", new File(System.getProperty("user.dir")).getParent());
    }

    public static void write(String arguments) throws IOException {
        String[] argArray = arguments.split(" ");
        String write;
        if(argArray.length > 3)
        {
            errorMessage(argArray[0]);
            return;
        }
        if(argArray.length<=2)
        {
            System.out.println("This command requires arguments.");
            return;
        }
        if(argArray[1].startsWith("\"") && argArray[1].endsWith("\""))
            write = argArray[1].substring(1, argArray[1].length()-1);
        else
        {
            System.out.println("Second argument needs to be between quotation marks.");
            return;
        }
        File file;
        if(argArray[2].contains(File.separator))
            file = new File(argArray[2]);
        else
            file = new File(System.getProperty("user.dir") + File.separator + argArray[2]);
        try {
            if (!file.isDirectory()) {
                if (!file.exists())
                    file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                bw.write(write);
                bw.close();
            } else {
                System.out.println("This command requires a file. Try again.");
                return;
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error with specified path. Try again.");
        }
    }


}
