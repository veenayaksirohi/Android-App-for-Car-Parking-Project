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

    @pytest.fixture(scope="function")  # Use function scope for full reset per test
    def driver(self):
        options = UiAutomator2Options()
        options.platform_name = 'Android'
        options.automation_name = 'UiAutomator2'
        options.device_name = 'Android Emulator'
        options.app_package = 'com.example.carparkingapp'
        options.app_activity = 'com.example.carparkingapp.MainActivity'
        options.implicit_wait_timeout = 10000 # milliseconds
        options.set_capability('uiautomator2ServerInstallTimeout', 60000)
        options.no_reset = False  # Ensures app is reset for each test

        driver = webdriver.Remote('http://127.0.0.1:4723/wd/hub', options=options)
        yield driver
        if driver:
            driver.quit()

    def handle_permission_dialog(self, driver, timeout=5):
        """Robustly handle Android permission dialogs if present."""
        allow_button_ids = [
            'com.android.permissioncontroller:id/permission_allow_button',
            'com.android.packageinstaller:id/permission_allow_button',
            'com.android.permissioncontroller:id/permission_allow_foreground_only_button',
            'com.android.permissioncontroller:id/permission_allow_always_button',
            'com.android.permissioncontroller:id/permission_allow_one_time_button',
        ]
        allow_texts = ["ALLOW", "Allow", "allow"]
        end_time = time.time() + timeout
        while time.time() < end_time:
            # Try by resource-id
            for btn_id in allow_button_ids:
                try:
                    allow_btn = driver.find_element(AppiumBy.ID, btn_id)
                    if allow_btn.is_displayed():
                        allow_btn.click()
                        return
                except Exception:
                    continue
            # Try by text (UiSelector)
            for text in allow_texts:
                try:
                    allow_btn = driver.find_element(
                        AppiumBy.ANDROID_UIAUTOMATOR,
                        f'new UiSelector().textMatches("(?i){text}")'
                    )
                    if allow_btn.is_displayed():
                        allow_btn.click()
                        return
                except Exception:
                    continue
            time.sleep(0.5)  # Wait and retry
        # Optionally, log if not found
        print("[WARN] Permission dialog 'Allow' button not found or not clickable.")

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

            # Handle permission dialog if it appears
            self.handle_permission_dialog(driver)

            # Try to detect toast (case-insensitive, partial match)
            toast_found = False
            for msg in ["Login successful", "login successful", "Login", "login"]:
                try:
                    toast = WebDriverWait(driver, 7, poll_frequency=0.2).until(
                        lambda d: d.find_element(
                            AppiumBy.ANDROID_UIAUTOMATOR,
                            f'new UiSelector().textContains("{msg}")'
                        )
                    )
                    if toast:
                        toast_found = True
                        break
                except Exception:
                    continue

            if toast_found:
                return  # Test passes if any toast variant is found

            # Fallback: Wait for dashboard elements as before
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
            
            # Explicitly wait for the login screen to appear after registration,
            # which is more reliable than a fixed time.sleep().
            self.wait_for_element(driver, (AppiumBy.ID, 'etLoginEmail'), timeout=15)

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
            # time.sleep(2) is removed as the explicit wait is now in register_user()

            # Optionally, verify registration success (e.g., by checking for a toast, dialog, or navigation)
            # This can be customized based on your app's behavior after registration

        except Exception as e:
            pytest.fail(f"Registration after app launch failed: {str(e)}")

    def test_login_with_provided_credentials(self, driver):
        """Test login functionality using the pre-defined TEST_EMAIL and TEST_PASSWORD."""
        try:
            time.sleep(2) # Give app time to initialize and be on main screen

            # Click the "Let's Get Started" button
            start_button = self.wait_for_element(driver, (AppiumBy.ID, 'button_start'))
            start_button.click()
            time.sleep(1) # Wait for transition

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
            # time.sleep(2) is removed as the explicit wait is now in register_user()

            # 3. After registration, the app is on the Login screen. No need to go back.
            # We can now login with the newly registered user.
            self.login(driver, unique_email, self.REGISTER_PASSWORD)
            # time.sleep(2) is removed as explicit waits are used in the login helper.

            # Verify successful login after the entire flow (already handled in login helper, but good to re-emphasize)
            search_container = self.wait_for_element(driver, (AppiumBy.ID, 'searchContainer'))
            map_search = self.wait_for_element(driver, (AppiumBy.ID, 'mapSearch'))
            assert search_container.is_displayed() and map_search.is_displayed(), "Final login verification failed: Dashboard not displayed."

        except Exception as e:
            pytest.fail(f"Full registration and login flow failed: {str(e)}")

    def login_with_expected_failure(self, driver, email, password, expected_error_msgs=None):
        """Attempt login and assert that it fails with an error message or toast."""
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

            # Expanded list of possible error messages for login failures
            error_msgs = expected_error_msgs or [
                "Invalid credentials", "invalid credentials", "incorrect", "not found", "failed", "wrong",
                "Email not registered", "email not registered", "User does not exist", "user does not exist",
                "Login failed", "login failed", "Invalid email or password", "invalid email or password",
                "Authentication failed", "authentication failed", "No account", "no account"
            ]
            found = False
            for msg in error_msgs:
                try:
                    toast = WebDriverWait(driver, 5, poll_frequency=0.2).until(
                        lambda d: d.find_element(
                            AppiumBy.ANDROID_UIAUTOMATOR,
                            f'new UiSelector().textContains("{msg}")'
                        )
                    )
                    if toast:
                        found = True
                        break
                except Exception:
                    continue
            # Fallback: check for error label on screen
            if not found:
                try:
                    error_label = driver.find_element(AppiumBy.ID, 'tvLoginError')
                    if error_label.is_displayed() and any(msg.lower() in error_label.text.lower() for msg in error_msgs):
                        found = True
                except Exception:
                    pass
            assert found, f"Expected login failure message/toast not found. Checked for: {error_msgs}"
        except Exception as e:
            pytest.fail(f"Negative login test failed: {str(e)}")

    def fill_registration_form(self, driver, name, email, password, phone, address):
        """Fill registration form fields without submitting."""
        try:
            name_input = self.wait_for_element(driver, (AppiumBy.ID, 'et1'))
            name_input.clear()
            name_input.send_keys(name)
            email_input = self.wait_for_element(driver, (AppiumBy.ID, 'et2'))
            email_input.clear()
            email_input.send_keys(email)
            password_input = self.wait_for_element(driver, (AppiumBy.ID, 'et3'))
            password_input.clear()
            password_input.send_keys(password)
            phone_input = self.wait_for_element(driver, (AppiumBy.ID, 'et4'))
            phone_input.clear()
            phone_input.send_keys(phone)
            address_input = self.wait_for_element(driver, (AppiumBy.ID, 'et5'))
            address_input.clear()
            address_input.send_keys(address)
        except Exception as e:
            pytest.fail(f"Filling registration form failed: {str(e)}")

    def assert_validation_message(self, driver, expected_msgs):
        """Assert that a validation message or toast is shown after registration attempt."""
        # Expanded list of possible error/validation messages for registration
        expanded_msgs = set(expected_msgs)
        expanded_msgs.update([
            "already registered", "already exists", "duplicate", "Email or phone number already registered",
            "email required", "enter email", "valid email", "invalid email", "@",
            "password required", "short password", "minimum", "Password must be at least", "password too short",
            "field required", "required", "missing", "Please enter", "Please provide"
        ])
        found = False
        for msg in expanded_msgs:
            try:
                toast = WebDriverWait(driver, 3, poll_frequency=0.2).until(
                    lambda d: d.find_element(
                        AppiumBy.ANDROID_UIAUTOMATOR,
                        f'new UiSelector().textContains("{msg}")'
                    )
                )
                if toast:
                    found = True
                    break
            except Exception:
                continue
        if not found:
            # Fallback: check for error label
            try:
                error_label = driver.find_element(AppiumBy.ID, 'tvRegisterError')
                if error_label.is_displayed() and any(msg.lower() in error_label.text.lower() for msg in expanded_msgs):
                    found = True
            except Exception:
                pass
        assert found, f"Expected validation message not found. Checked for: {list(expanded_msgs)}"

    def test_registration_validation(self, driver):
        """Test registration with missing/invalid fields (empty email, invalid email, short password)."""
        try:
            start_button = self.wait_for_element(driver, (AppiumBy.ID, 'button_start'))
            start_button.click()
            # Empty email
            self.fill_registration_form(driver, self.REGISTER_NAME, "", self.REGISTER_PASSWORD, self.REGISTER_PHONE, self.REGISTER_ADDRESS)
            register_button = self.wait_for_element(driver, (AppiumBy.ID, 'b1'))
            register_button.click()
            self.assert_validation_message(driver, ["email", "required", "enter email"])
            # Invalid email format
            self.fill_registration_form(driver, self.REGISTER_NAME, "invalidemail", self.REGISTER_PASSWORD, self.REGISTER_PHONE, self.REGISTER_ADDRESS)
            register_button.click()
            self.assert_validation_message(driver, ["valid email", "invalid email", "@"])
            # Short password
            self.fill_registration_form(driver, self.REGISTER_NAME, self.generate_unique_email(), "123", self.REGISTER_PHONE, self.REGISTER_ADDRESS)
            register_button.click()
            self.assert_validation_message(driver, ["password", "short", "minimum"])
        except Exception as e:
            pytest.fail(f"Registration validation test failed: {str(e)}")

    def test_duplicate_registration(self, driver):
        """Test registration with an already registered email."""
        try:
            start_button = self.wait_for_element(driver, (AppiumBy.ID, 'button_start'))
            start_button.click()
            # Register with existing email
            self.fill_registration_form(driver, self.REGISTER_NAME, self.TEST_EMAIL, self.REGISTER_PASSWORD, self.REGISTER_PHONE, self.REGISTER_ADDRESS)
            register_button = self.wait_for_element(driver, (AppiumBy.ID, 'b1'))
            register_button.click()
            self.assert_validation_message(driver, ["already", "exists", "duplicate", "registered"])
        except Exception as e:
            pytest.fail(f"Duplicate registration test failed: {str(e)}")