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
 * Implementation of skin-related actions for the Colibri skin.
 * 
 * @version $Id$
 */
public class ColibriSkinExecutor extends AlbatrossSkinExecutor
{
    public ColibriSkinExecutor(AbstractXWikiTestCase test)
    {
        super(test);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAuthenticated()
    {
        return getTest().isElementPresent("tmUser");
    }

    /**
     * {@inheritDoc}
     *
     * @see SkinExecutor#clickShowComments() 
     */
    public void clickShowComments()
    {
        getTest().clickLinkWithLocator("//span[@id = 'commentsshortcut']/a", false);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.xpn.xwiki.it.selenium.framework.SkinExecutor#clickShowAttachments()
     */
    public void clickShowAttachments()
    {
        getTest().clickLinkWithLocator("//span[@id = 'attachmentsshortcut']/a", false);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.xpn.xwiki.it.selenium.framework.SkinExecutor#clickShowHistory()
     */
    public void clickShowHistory()
    {
        getTest().clickLinkWithLocator("//span[@id = 'historyshortcut']/a", false);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.xpn.xwiki.it.selenium.framework.SkinExecutor#clickShowInformation()
     */
    public void clickShowInformation()
    {
        getTest().clickLinkWithLocator("//span[@id = 'informationshortcut']/a", false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loginAsAdmin()
    {
        // Verify if the login or logout links are available and if not go to the home page to make it available
        // (for ex it's not available in edit mode)
        if (!getTest().isElementPresent("//div[@id='tmLogin' or @id='tmLogout']/a")) {
            getTest().open("Main", "WebHome");
        }
        
        // First verify if the logged in user is not already the Administrator. That'll save us execution time.
        if (!getTest().isElementPresent("//div[@id='tmUser']/a[contains(@href, '/xwiki/bin/view/XWiki/Admin')]")) {
            login("Admin", "admin", false);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void login(String username, String password, boolean rememberme)
    {
        // Verify if the login or logout links are available and if not go to the home page to make it available
        // (for ex it's not available in edit mode)
        if (!getTest().isElementPresent("//div[@id='tmLogin' or @id='tmLogout']/a")) {
            getTest().open("Main", "WebHome");
        }
        
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void logout()
    {
        Assert.assertTrue("User wasn't authenticated.", isAuthenticated());
        getTest().clickLinkWithLocator("//div[@id='tmLogout']/a");
        Assert.assertFalse("The user is still authenticated after a logout.", isAuthenticated());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void clickLogin()
    {
        getTest().clickLinkWithLocator("//div[@id='tmLogin']/a");
        assertIsLoginPage();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void clickRegister()
    {
        getTest().clickLinkWithLocator("//div[@id='tmRegister']/a");
        assertIsRegisterPage();
    }
    
}