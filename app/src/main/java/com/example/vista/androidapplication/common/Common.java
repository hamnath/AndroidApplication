package com.example.vista.androidapplication.common;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

public class Common {

    OystorApp OA = new OystorApp();
    public void viewDocument(Context context,String paths)
    {
        try {

            System.out.println("Entered in View Document");

            File file = new File(paths);

            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(paths));

                if(!mimeType.equals(""))
                {
                    intent.setDataAndType(path, mimeType);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
                else
                {
                    OA.alertToast("Unsupported Format to Open", context);
                }
            }
        }
        catch (ActivityNotFoundException e) {
            OA.alertToast("No Application Available to View this File",  context);
        }
        catch(Exception e)
        {
            OA.alertToast("No Application Available to View this File",  context);
        }
    }
}
