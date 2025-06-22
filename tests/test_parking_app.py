import pytest
import time
import uuid
import random
from appium import webdriver
from appium.webdriver.common.appiumby import AppiumBy
from appium.options.android import UiAutomator2Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, NoSuchElementException

class TestParkingApp:
    # Test credentials for an existing user (if needed for other tests in future)
    TEST_EMAIL = "alice.sharma@example.com"
    TEST_PASSWORD = "secret123"

    # Test credentials for new registration
    REGISTER_NAME = "New User"
    REGISTER_PASSWORD = "new_secret123"
    REGISTER_PHONE = "0987654321"
    REGISTER_ADDRESS = "456 New St"

    # Helper method for explicit waits
    def wait_for_element(self, driver, locator, timeout=10):
        try:
            element = WebDriverWait(driver, timeout).until(
                EC.presence_of_element_located(locator)
            )
            return element
        except TimeoutException:
            pytest.fail(f"Timeout: Element with locator {locator} not found after {timeout} seconds.")
        except NoSuchElementException:
            pytest.fail(f"No Such Element: Element with locator {locator} not found.")
        except Exception as e:
            pytest.fail(f"Error finding element {locator}: {str(e)}")

    def generate_unique_email(self):
        """Generate a unique email address for registration tests."""
        return f"testuser_{uuid.uuid4().hex[:8]}@example.com"

    def generate_unique_phone(self):
        """Generate a unique phone number for registration tests."""
        return f"9{random.randint(100000000, 999999999)}"

    @pytest.fixture(scope="class")
    def driver(self):
        options = UiAutomator2Options()
        options.platform_name = 'Android'
        options.automation_name = 'UiAutomator2'
        options.device_name = 'Android Emulator'
        options.app_package = 'com.example.carparkingapp'
        options.app_activity = 'com.example.carparkingapp.MainActivity'
        options.implicit_wait_timeout = 10000 # milliseconds
        options.set_capability('uiautomator2ServerInstallTimeout', 60000)

        # Connect to Appium server
        # Update the URL if your Appium server is running on a different host or port
        driver = webdriver.Remote('http://127.0.0.1:4723', options=options)

        yield driver

        if driver:
            driver.quit()

    def login(self, driver, email, password):
        """Helper method to perform login, assumes already on login screen"""
        try:
            # Enter email
            email_input = self.wait_for_element(driver, (AppiumBy.ID, 'etLoginEmail'))
            email_input.clear()
            email_input.send_keys(email)

            # Enter password
            password_input = self.wait_for_element(driver, (AppiumBy.ID, 'etLoginPassword'))
            password_input.clear()
            password_input.send_keys(password)

            # Click login submit button
            submit_button = self.wait_for_element(driver, (AppiumBy.ID, 'btnLogin'))
            submit_button.click()

            # Wait for login to complete
            time.sleep(2)

            # Verify successful login (e.g., dashboard elements)
            search_container = self.wait_for_element(driver, (AppiumBy.ID, 'searchContainer'))
            map_search = self.wait_for_element(driver, (AppiumBy.ID, 'mapSearch'))
            assert search_container.is_displayed() and map_search.is_displayed(), "Login failed or dashboard not displayed"

        except Exception as e:
            pytest.fail(f"Login failed: {str(e)}")

    def register_user(self, driver, name, email, password, phone, address):
        """Helper method to perform user registration, assumes already on registration screen"""
        try:
            # Enter Name
            name_input = self.wait_for_element(driver, (AppiumBy.ID, 'et1'))
            name_input.clear()
            name_input.send_keys(name)

            # Enter Email
            email_input = self.wait_for_element(driver, (AppiumBy.ID, 'et2'))
            email_input.clear()
            email_input.send_keys(email)

            # Enter Password
            password_input = self.wait_for_element(driver, (AppiumBy.ID, 'et3'))
            password_input.clear()
            password_input.send_keys(password)

            # Enter Phone No
            phone_input = self.wait_for_element(driver, (AppiumBy.ID, 'et4'))
            phone_input.clear()
            phone_input.send_keys(phone)

            # Enter Address
            address_input = self.wait_for_element(driver, (AppiumBy.ID, 'et5'))
            address_input.clear()
            address_input.send_keys(address)

            # Click Register button
            register_button = self.wait_for_element(driver, (AppiumBy.ID, 'b1'))
            register_button.click()
            time.sleep(2) # Wait for registration to complete

            # Assuming successful registration might navigate to the login screen or display a message.
            # For this test, we expect to go back to the main screen to start login.

        except Exception as e:
            pytest.fail(f"Registration failed: {str(e)}")

    def test_app_launch(self, driver):
        """Test that the app launches successfully and verifies initial elements"""
        try:
            time.sleep(2)
            
            # Verify main title
            title_locator = (AppiumBy.ID, 'text_main_title')
            element = self.wait_for_element(driver, title_locator)
            assert element.is_displayed(), "Main title is not displayed"
            assert element.text == "VisionPark", f"Expected title 'VisionPark', got '{element.text}'"

            # Verify subtitle
            subtitle_locator = (AppiumBy.ID, 'text_subtitle')
            element = self.wait_for_element(driver, subtitle_locator)
            assert element.is_displayed(), "Subtitle is not displayed"
            assert element.text == "Find your spot", f"Expected subtitle 'Find your spot', got '{element.text}'"

            # Verify "Let's Get Started" button
            start_button = self.wait_for_element(driver, (AppiumBy.ID, 'button_start'))
            assert start_button.is_displayed(), "Start button is not displayed"
            assert start_button.text == "Let's Get Started", f"Expected button text 'Let's Get Started', got '{start_button.text}'"

        except Exception as e:
            pytest.fail(f"App launch test failed: {str(e)}")

    def test_registration_after_app_launch(self, driver):
        """Test registration flow immediately after launching the app."""
        try:
            # Click the "Let's Get Started" button to go to Register Form
            start_button = self.wait_for_element(driver, (AppiumBy.ID, 'button_start'))
            start_button.click()

            # Register a new user with a unique email and phone number
            unique_email = self.generate_unique_email()
            unique_phone = self.generate_unique_phone()
            self.register_user(driver, self.REGISTER_NAME, unique_email,
                               self.REGISTER_PASSWORD, unique_phone, self.REGISTER_ADDRESS)
            time.sleep(2)  # Wait after registration

            # Optionally, verify registration success (e.g., by checking for a toast, dialog, or navigation)
            # This can be customized based on your app's behavior after registration

        except Exception as e:
            pytest.fail(f"Registration after app launch failed: {str(e)}")

    def test_login_with_provided_credentials(self, driver):
        """Test login functionality using the pre-defined TEST_EMAIL and TEST_PASSWORD."""
        try:
            time.sleep(2)  # Give app time to initialize and be on main screen

            # Click the "Let's Get Started" button
            start_button = self.wait_for_element(driver, (AppiumBy.ID, 'button_start'))
            start_button.click()
            time.sleep(1)  # Wait for transition

            # Click the "Sign In" link to navigate to the login form
            sign_in_link = self.wait_for_element(driver, (AppiumBy.ID, 'tv3'))
            sign_in_link.click()
            time.sleep(1) # Wait for transition to login screen

            # Perform login with provided credentials
            self.login(driver, self.TEST_EMAIL, self.TEST_PASSWORD)

            # Verification of successful login is already handled within the login helper

        except Exception as e:
            pytest.fail(f"Login with provided credentials test failed: {str(e)}")

    def test_full_registration_and_login_flow(self, driver):
        """Test the complete flow: launch -> click start -> register -> back -> click start -> sign in -> login"""
        try:
            time.sleep(2) # Give app time to initialize and be on main screen

            # 1. Click the "Let's Get Started" button to go to Register Form
            start_button = self.wait_for_element(driver, (AppiumBy.ID, 'button_start'))
            start_button.click()
            time.sleep(1) # Wait for transition to Register Form

            # 2. Register a new user with a unique email and phone number
            unique_email = self.generate_unique_email()
            unique_phone = self.generate_unique_phone()
            self.register_user(driver, self.REGISTER_NAME, unique_email,
                               self.REGISTER_PASSWORD, unique_phone, self.REGISTER_ADDRESS)
            time.sleep(2) # Wait after registration

            # 3. Go back to the main screen after registration (assuming it stays on register screen or goes to a confirmation)
            # This should take us back to the main launch screen
            driver.back() 
            time.sleep(2) # Wait for transition back

            # 4. Click "Let's Get Started" again to go to Register Form, then click "Sign In"
            start_button_after_back = self.wait_for_element(driver, (AppiumBy.ID, 'button_start'))
            start_button_after_back.click()
            time.sleep(1) # Wait for transition

            sign_in_link = self.wait_for_element(driver, (AppiumBy.ID, 'tv3'))
            sign_in_link.click()
            time.sleep(1) # Wait for transition to Login screen

            # 5. Login with the newly registered user
            self.login(driver, unique_email, self.REGISTER_PASSWORD)
            time.sleep(2) # Wait for login completion

            # Verify successful login after the entire flow (already handled in login helper, but good to re-emphasize)
            search_container = self.wait_for_element(driver, (AppiumBy.ID, 'searchContainer'))
            map_search = self.wait_for_element(driver, (AppiumBy.ID, 'mapSearch'))
            assert search_container.is_displayed() and map_search.is_displayed(), "Final login verification failed: Dashboard not displayed."

        except Exception as e:
            pytest.fail(f"Full registration and login flow failed: {str(e)}")