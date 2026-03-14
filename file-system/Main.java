public class Main {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        run("rootPath", Main::rootPath);
        run("createFoldersAndFiles", Main::createFoldersAndFiles);
        run("renameAndMove", Main::renameAndMove);
        run("deleteAndErrors", Main::deleteAndErrors);
        run("preventMoveFolderIntoItself", Main::preventMoveFolderIntoItself);

        System.out.println();
        System.out.println("Passed: " + passed + ", Failed: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void run(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("[PASS] " + name);
        } catch (Throwable t) {
            failed++;
            System.out.println("[FAIL] " + name + " -> " + t.getClass().getSimpleName() + ": " + t.getMessage());
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void expectThrows(Class<? extends Throwable> type, Runnable r) {
        try {
            r.run();
        } catch (Throwable t) {
            if (type.isInstance(t)) {
                return;
            }
            throw new AssertionError("Expected " + type.getSimpleName() + " but got " + t.getClass().getSimpleName(), t);
        }
        throw new AssertionError("Expected " + type.getSimpleName() + " but nothing was thrown");
    }

    private static void rootPath() {
        FileSystem fs = new FileSystem();
        check("/".equals(fs.getRoot().getPath()), "Root path should be /");
        check(fs.getEntry("/").isDirectory(), "Root should be a directory");
    }

    private static void createFoldersAndFiles() {
        FileSystem fs = new FileSystem();
        fs.createFolder("/a");
        fs.createFolder("/a/b");
        File f = fs.createFile("/a/b/hello.txt", "hello");

        check("/a/b/hello.txt".equals(f.getPath()), "File path incorrect");
        check("hello".equals(f.getContent()), "File content incorrect");
        check(fs.getEntry("/a").isDirectory(), "/a should be a folder");
        check(!fs.getEntry("/a/b/hello.txt").isDirectory(), "File should not be a directory");
    }

    private static void renameAndMove() {
        FileSystem fs = new FileSystem();
        fs.createFolder("/src");
        fs.createFolder("/dst");
        fs.createFile("/src/a.txt", "a");

        fs.rename("/src/a.txt", "b.txt");
        check("/src/b.txt".equals(fs.getEntry("/src/b.txt").getPath()), "Rename should update path");
        expectThrows(IllegalArgumentException.class, () -> fs.getEntry("/src/a.txt"));

        fs.move("/src/b.txt", "/dst/c.txt");
        check("/dst/c.txt".equals(fs.getEntry("/dst/c.txt").getPath()), "Move should update path");
        expectThrows(IllegalArgumentException.class, () -> fs.getEntry("/src/b.txt"));
    }

    private static void deleteAndErrors() {
        FileSystem fs = new FileSystem();
        fs.createFolder("/a");
        fs.createFile("/a/x", "x");

        fs.delete("/a/x");
        expectThrows(IllegalArgumentException.class, () -> fs.getEntry("/a/x"));

        expectThrows(IllegalArgumentException.class, () -> fs.delete("/"));
        expectThrows(IllegalArgumentException.class, () -> fs.createFile("/", "nope"));
        expectThrows(IllegalArgumentException.class, () -> fs.getEntry("relative/path"));
        expectThrows(IllegalArgumentException.class, () -> fs.getEntry("//double"));
    }

    private static void preventMoveFolderIntoItself() {
        FileSystem fs = new FileSystem();
        fs.createFolder("/a");
        fs.createFolder("/a/b");

        expectThrows(IllegalArgumentException.class, () -> fs.move("/a", "/a/b/a"));
    }
}