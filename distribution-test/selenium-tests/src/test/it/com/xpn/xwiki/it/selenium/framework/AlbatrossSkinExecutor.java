/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
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
package com.xpn.xwiki.it.selenium.framework;

import junit.framework.Assert;

/**
 * Implementation of skin-related actions for the Albatross skin.
 * 
 * @version $Id: $
 */
public class AlbatrossSkinExecutor implements SkinExecutor
{
    private static final String WYSIWYG_LOCATOR_FOR_KEY_EVENTS = "mceSpanFonts";

    private static final String WIKI_LOCATOR_FOR_KEY_EVENTS = "content";

    private AbstractXWikiTestCase test;

    public AlbatrossSkinExecutor(AbstractXWikiTestCase test)
    {
        this.test = test;
    }

    private AbstractXWikiTestCase getTest()
    {
        return this.test;
    }

    /**
     * {@inheritDoc}
     * 
     * @see SkinExecutor#clickEditPage()
     */
    public void clickEditPage()
    {
        getTest().clickLinkWithLocator("//a[string() = 'Edit']");
    }

    public void clickDeletePage()
    {
        getTest().clickLinkWithLocator("//a[string() = 'Delete']");
    }

    public void clickCopyPage()
    {
        getTest().clickLinkWithLocator("//a[string() = 'Copy']");
    }

    public void clickEditPreview()
    {
        getTest().submit("xpath=//input[@name='formactionpreview' or @name='action_preview']");
    }

    public void clickEditSaveAndContinue()
    {
        if (getTest().isElementPresent("xpath=//input[@name='formactionsac']")) {
            getTest().submit("xpath=//input[@name='formactionsac']");
        } else {
            getTest().submit("xpath=//input[@name='action_saveandcontinue']", false);
            getTest().getSelenium().waitForCondition("selenium.isVisible(\"xpath=//span[string(.) = 'Saved']\")",
                "5000");
        }
    }

    public void clickEditCancelEdition()
    {
        getTest().submit("xpath=//input[@name='formactioncancel' or @name='action_cancel']");
    }

    public void clickEditSaveAndView()
    {
        getTest().submit("xpath=//input[@name='formactionsave' or @name='action_save' or @name='action_propupdate']");
    }

    public boolean isAuthenticated()
    {
        return !getTest().isElementPresent("headerlogin") && !getTest().isElementPresent("headerregister");
    }

    public void logout()
    {
        Assert.assertTrue("User wasn't authenticated.", isAuthenticated());
        getTest().clickLinkWithLocator("headerlogout");
        Assert.assertFalse("The user is still authenticated after a logout.", isAuthenticated());
    }

    public void login(String username, String password, boolean rememberme)
    {
        getTest().open("Main", "WebHome");

        if (isAuthenticated()) {
            logout();
        }

        clickLogin();

        getTest().setFieldValue("j_username", username);
        getTest().setFieldValue("j_password", password);
        if (rememberme) {
            getTest().checkField("rememberme");
        }
        getTest().submit();

        Assert.assertTrue("User has not been authenticated", isAuthenticated());
    }

    public void loginAsAdmin()
    {
        // First verify if the logged in user is not already the Administrator. That'll save us execution time.
        if (!getTest().isElementPresent("//a[@id='headeruser' and contains(@href, 'XWiki/Admin')]")) {
            login("Admin", "admin", false);
        }
    }

    public void clickLogin()
    {
        getTest().clickLinkWithLocator("headerlogin");
        assertIsLoginPage();
    }

    private void assertIsLoginPage()
    {
        getTest().assertElementPresent("loginForm");
        getTest().assertElementPresent("j_username");
        getTest().assertElementPresent("j_password");
        getTest().assertElementPresent("rememberme");
    }

    public void clickRegister()
    {
        getTest().clickLinkWithLocator("headerregister");
        assertIsRegisterPage();
    }

    private void assertIsRegisterPage()
    {
        // tests that the register form exists
        getTest().assertElementPresent("register");
        getTest().assertElementPresent("register_first_name");
        getTest().assertElementPresent("register_last_name");
        getTest().assertElementPresent("xwikiname");
        getTest().assertElementPresent("register_password");
        getTest().assertElementPresent("register2_password");
        getTest().assertElementPresent("register_email");
    }

    // For WYSIWYG editor

    public void editInWysiwyg(String space, String page)
    {
        getTest().open("/xwiki/bin/edit/" + space + "/" + page + "?editor=wysiwyg");
    }

    public void clearWysiwygContent()
    {
        getTest().waitForCondition("selenium.browserbot.getCurrentWindow().tinyMCE.setContent(\"\"); true");
    }

    public void typeInWysiwyg(String text)
    {
        getTest().getSelenium().typeKeys(WYSIWYG_LOCATOR_FOR_KEY_EVENTS, text);
    }

    public void typeInWiki(String text)
    {
        getTest().getSelenium().type(WIKI_LOCATOR_FOR_KEY_EVENTS, text);
    }

    public void typeEnterInWysiwyg()
    {
        getTest().getSelenium().keyPress(WYSIWYG_LOCATOR_FOR_KEY_EVENTS, "\\13");
    }

    public void typeShiftEnterInWysiwyg()
    {
        getTest().getSelenium().shiftKeyDown();
        getTest().getSelenium().keyPress(WYSIWYG_LOCATOR_FOR_KEY_EVENTS, "\\13");
        getTest().getSelenium().shiftKeyUp();
    }

    public void clickWysiwygUnorderedListButton()
    {
        getTest().clickLinkWithLocator("//img[@title='Unordered list']", false);
    }

    public void clickWysiwygOrderedListButton()
    {
        getTest().clickLinkWithLocator("//img[@title='Ordered list']", false);
    }

    public void clickWysiwygIndentButton()
    {
        getTest().clickLinkWithLocator("//img[@title='Indent']", false);
    }

    public void clickWysiwygOutdentButton()
    {
        getTest().clickLinkWithLocator("//img[@title='Outdent']", false);
    }

    public void clickWikiBoldButton()
    {
        getTest().clickLinkWithXPath("//img[@title='Bold']", false);
    }

    public void clickWikiItalicsButton()
    {
        getTest().clickLinkWithXPath("//img[@title='Italics']", false);
    }

    public void clickWikiUnderlineButton()
    {
        getTest().clickLinkWithXPath("//img[@title='Underline']", false);
    }

    public void clickWikiLinkButton()
    {
        getTest().clickLinkWithXPath("//img[@title='Internal Link']", false);
    }

    public void clickWikiHRButton()
    {
        getTest().clickLinkWithXPath("//img[@title='Horizontal ruler']", false);
    }

    public void clickWikiImageButton()
    {
        getTest().clickLinkWithXPath("//img[@title='Attached Image']", false);
    }

    public void clickWikiSignatureButton()
    {
        getTest().clickLinkWithXPath("//img[@title='Signature']", false);
    }

    public void assertWikiTextGeneratedByWysiwyg(String text)
    {
        getTest().clickLinkWithText("Wiki");
        Assert.assertEquals(text, getTest().getSelenium().getValue("content"));
    }

    public void assertHTMLGeneratedByWysiwyg(String xpath) throws Exception
    {
        getTest().getSelenium().selectFrame("mce_editor_0");
        Assert.assertTrue(getTest().getSelenium().isElementPresent("xpath=/html/body/" + xpath));
        getTest().getSelenium().selectFrame("relative=top");
    }

    public void assertGeneratedHTML(String xpath) throws Exception
    {
        Assert.assertTrue(getTest().getSelenium().isElementPresent("xpath=//div[@id='xwikicontent']/" + xpath));
    }

    public void openAdministrationPage()
    {
        getTest().open("XWiki", "XWikiPreferences", "admin");
    }
    
    public void openAdministrationSection(String section)
    {
        this.openAdministrationPage();
        
        getTest().clickLinkWithLocator("//li[@class='" + section + "']/a");
    }

    public void pressKeyboardShortcut(String shortcut, boolean withCtrlModifier, boolean withAltModifier,
        boolean withShiftModifier) throws InterruptedException
    {
        if (withCtrlModifier) {
            getTest().getSelenium().controlKeyDown();
        }
        if (withAltModifier) {
            getTest().getSelenium().altKeyDown();
        }
        if (withShiftModifier) {
            getTest().getSelenium().shiftKeyDown();
        }
        getTest().getSelenium().keyPress("xwikimaincontainer", shortcut);
        if (withCtrlModifier) {
            getTest().getSelenium().controlKeyUp();
        }
        if (withAltModifier) {
            getTest().getSelenium().altKeyUp();
        }
        if (withShiftModifier) {
            getTest().getSelenium().shiftKeyUp();
        }
    }
}
