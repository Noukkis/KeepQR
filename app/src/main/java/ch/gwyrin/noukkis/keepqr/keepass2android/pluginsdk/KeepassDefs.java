/*
 * The MIT License
 *
 * Copyright (c) 2018 Noukkis.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package ch.gwyrin.noukkis.keepqr.keepass2android.pluginsdk;

public class KeepassDefs {

	/// <summary>
	/// Default identifier string for the title field. Should not contain
	/// spaces, tabs or other whitespace.
	/// </summary>
	public static String TitleField = "Title";

	/// <summary>
	/// Default identifier string for the user name field. Should not contain
	/// spaces, tabs or other whitespace.
	/// </summary>
	public static String UserNameField = "UserName";

	/// <summary>
	/// Default identifier string for the password field. Should not contain
	/// spaces, tabs or other whitespace.
	/// </summary>
	public static String PasswordField = "Password";

	/// <summary>
	/// Default identifier string for the URL field. Should not contain
	/// spaces, tabs or other whitespace.
	/// </summary>
	public static String UrlField = "URL";

	/// <summary>
	/// Default identifier string for the notes field. Should not contain
	/// spaces, tabs or other whitespace.
	/// </summary>
	public static String NotesField = "Notes";

	
	public static boolean IsStandardField(String strFieldName)
	{
		if(strFieldName == null)
			return false;
		if(strFieldName.equals(TitleField)) return true;
		if(strFieldName.equals(UserNameField)) return true;
		if(strFieldName.equals(PasswordField)) return true;
		if(strFieldName.equals(UrlField)) return true;
		if(strFieldName.equals(NotesField)) return true;

		return false;
	}
}
