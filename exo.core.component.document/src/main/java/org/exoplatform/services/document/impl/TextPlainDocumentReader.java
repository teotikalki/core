/*
 * Copyright (C) 2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.services.document.impl;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValuesParam;
import org.exoplatform.services.document.DocumentReadException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by The eXo Platform SAS A reader of text files.
 * 
 * @author <a href="mailto:zagrebin_v@mail.ru">Victor Zagrebin</a>
 * @version March 04, 2006
 */
public class TextPlainDocumentReader extends BaseDocumentReader
{

   public static final String DEFAULT_ENCODING = "defaultEncoding";

   private String defaultEncoding;

   /**
    * Initializes a newly created object for text/plain files format parsing.
    * 
    * @param params the container parameters.
    */
   public TextPlainDocumentReader(InitParams params)
   {

      ValuesParam encoding = (ValuesParam)params.getParameter(DEFAULT_ENCODING);

      if (encoding != null && encoding.getValue() != null && !encoding.getValue().equalsIgnoreCase(""))
      {
         defaultEncoding = encoding.getValue();
      }
      else
      {
         defaultEncoding = null;
      }
   }

   /**
    * Get the "text/plain","script/groovy","application/x-groovy","application/x-javascript","application/javascript","text/javascript" mime types.
    * 
    * @return The "text/plain","script/groovy","application/x-groovy","application/x-javascript","application/javascript","text/javascript" mime type.
    */
   public String[] getMimeTypes()
   {
      return new String[]{"text/plain","script/groovy",
                          "application/x-groovy","application/x-javascript",
                          "application/javascript","text/javascript",
                          "application/x-jaxrs+groovy"};
   }

   /**
    * Returns a text from file content.
    * 
    * @param is an input stream with a file content.
    * @return The string with text from file content.
    */
   public String getContentAsText(InputStream is) throws IOException, DocumentReadException
   {
      if (this.defaultEncoding != null)
      {
         return getContentAsText(is, defaultEncoding);
      }
      else
      {
         return getContentAsText(is, null); // system encoding will be used
      }
   }

   /**
    * Returns a text from file content. If encoding parameter is null - system
    * encoding will be used.
    * 
    * @param is an input stream with a file content.
    * @param encoding file content encoding.
    * @return The string with text from file content.
    */
   public String getContentAsText(InputStream is, String encoding) throws IOException, DocumentReadException
   {
      if (is == null)
      {
         throw new NullPointerException("InputStream is null.");
      }

      try
      {
         byte[] buffer = new byte[2048];
         int len;
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         while ((len = is.read(buffer)) > 0)
            bos.write(buffer, 0, len);
         bos.close();

         if (bos.size() == 0)
            return "";
         else if (encoding != null)
            return new String(bos.toByteArray(), encoding);
         else
            return new String(bos.toByteArray());
      }
      finally
      {
         if (is != null)
            try
            {
               is.close();
            }
            catch (IOException e)
            {
            }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.exoplatform.services.document.DocumentReader#getProperties(java.io.
    *      InputStream)
    */
   public Properties getProperties(InputStream is) throws IOException, DocumentReadException
   {

      try
      {
         is.close();
      }
      catch (IOException e)
      {
      }
      return new Properties();
   }

}
