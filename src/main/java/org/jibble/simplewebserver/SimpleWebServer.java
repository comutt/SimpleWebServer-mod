/**
Copyright KOMATSU Seiji (comutt), 2012, https://github.com/comutt/SimpleWebServer-mod

This software is modification of the original from http://www.jibble.org/miniwebserver/,
and licensed under the GNU General Public License (GPL) from the Free Software Foundation, http://www.fsf.org/.

Thanks to Paul James Mutton, the author of the original.

The original copyright is as follows:

  Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/

  This file is part of Mini Wegb Server / SimpleWebServer.

  This software is dual-licensed, allowing you to choose between the GNU
  General Public License (GPL) and the www.jibble.org Commercial License.
  Since the GPL may be too restrictive for use in a proprietary application,
  a commercial license is also provided. Full license information can be
  found at http://www.jibble.org/licenses/

*/

package org.jibble.simplewebserver;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright Paul Mutton
 * http://www.jibble.org/
 *
 */
public class SimpleWebServer extends Thread {

    public static final String VERSION = "SimpleWebServer-mod";
    public static final Map<String, String> MIME_TYPES = new HashMap<String, String>();

    static {
        final String image = "image/";
        MIME_TYPES.put(".gif", image + "gif");
        MIME_TYPES.put(".jpg", image + "jpeg");
        MIME_TYPES.put(".jpeg", image + "jpeg");
        MIME_TYPES.put(".png", image + "png");
        final String text = "text/";
        MIME_TYPES.put(".html", text + "html");
        MIME_TYPES.put(".htm", text + "html");
        MIME_TYPES.put(".txt", text + "plain");
        MIME_TYPES.put(".css", text + "css");
        final String application = "application";
        MIME_TYPES.put(".js", application + "javascript");
    }

    public SimpleWebServer(File rootDir, int port) throws IOException {
        _rootDir = rootDir.getCanonicalFile();
        if (!_rootDir.isDirectory()) {
            throw new IOException("Not a directory.");
        }
        _serverSocket = new ServerSocket(port);
    }

    @Override
	public void run() {
        while (_running) {
            try {
                Socket socket = _serverSocket.accept();
                RequestThread requestThread = new RequestThread(socket, _rootDir);
                requestThread.start();
            }
            catch (IOException e) {
                System.exit(1);
            }
        }
    }

    // Work out the filename extension.  If there isn't one, we keep
    // it as the empty string ("").
    public static String getExtension(java.io.File file) {
        String extension = "";
        String filename = file.getName();
        int dotPos = filename.lastIndexOf(".");
        if (dotPos >= 0) {
            extension = filename.substring(dotPos);
        }
        return extension.toLowerCase();
    }

    private final File _rootDir;
    private final ServerSocket _serverSocket;
    private final boolean _running = true;

}