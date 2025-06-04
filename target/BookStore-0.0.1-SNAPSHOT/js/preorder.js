/**
 * Book preorder functionality - handles both user account and email-based preorders
 */

// Function to show the email preorder modal dialog
function showEmailPreorderModal(bookId, bookTitle) {
    // Create the modal HTML
    const modalHTML = `
    <div class="modal fade" id="emailPreorderModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Preorder Book</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <p>Enter your email to be notified when "${bookTitle}" becomes available:</p>
            <div class="mb-3">
              <label for="preorderName" class="form-label">Your Name (optional)</label>
              <input type="text" class="form-control" id="preorderName">
            </div>
            <div class="mb-3">
              <label for="preorderEmail" class="form-label">Your Email *</label>
              <input type="email" class="form-control" id="preorderEmail" required>
              <div class="invalid-feedback">
                Please enter a valid email address
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
            <button type="button" class="btn btn-primary" id="submitPreorderEmail">Preorder</button>
          </div>
        </div>
      </div>
    </div>
    `;
    
    // Append modal to body if it doesn't already exist
    if (!document.getElementById('emailPreorderModal')) {
        document.body.insertAdjacentHTML('beforeend', modalHTML);
    }
    
    // Initialize the modal
    const modal = new bootstrap.Modal(document.getElementById('emailPreorderModal'));
    
    // Clear previous inputs
    document.getElementById('preorderEmail').value = '';
    if (document.getElementById('preorderName')) {
        document.getElementById('preorderName').value = '';
    }
    
    // Setup submit button handler
    const submitButton = document.getElementById('submitPreorderEmail');
    submitButton.onclick = function() {
        submitEmailPreorder(bookId, modal);
    };
    
    // Show the modal
    modal.show();
}

// Function to submit the email-based preorder
function submitEmailPreorder(bookId, modal) {
    const emailInput = document.getElementById('preorderEmail');
    const nameInput = document.getElementById('preorderName');
    const email = emailInput.value.trim();
    const name = nameInput ? nameInput.value.trim() : '';
    
    // Validate email
    if (!email || !email.includes('@')) {
        emailInput.classList.add('is-invalid');
        return;
    }
    
    // Remove invalid class if present
    emailInput.classList.remove('is-invalid');
    
    // AJAX request to submit preorder
    $.ajax({
        url: 'PreorderServlet',
        type: 'POST',
        data: {
            action: 'emailPreorder',
            bookId: bookId,
            email: email,
            name: name
        },
        success: function(response) {
            // Hide modal
            modal.hide();
            
            // Show result
            if (response.success) {
                showNotification('success', response.message);
            } else {
                showNotification('danger', response.message);
            }
        },
        error: function() {
            modal.hide();
            showNotification('danger', 'Error processing your request. Please try again later.');
        }
    });
}

// Function to handle the preorder button click
function handlePreorderClick(bookId, bookTitle) {
    // First check if user is logged in
    $.ajax({
        url: 'PreorderServlet',
        type: 'POST',
        data: { action: 'checkLoginStatus' },
        success: function(response) {
            if (response.success) {
                // User is logged in, process account-based preorder
                submitAccountPreorder(bookId);
            } else {
                // User is not logged in, show email input modal
                showEmailPreorderModal(bookId, bookTitle);
            }
        },
        error: function() {
            // On error, default to email preorder
            showEmailPreorderModal(bookId, bookTitle);
        }
    });
}

// Function to submit preorder using user account
function submitAccountPreorder(bookId) {
    $.ajax({
        url: 'PreorderServlet',
        type: 'POST',
        data: {
            action: 'add',
            bookId: bookId
        },
        success: function(response) {
            if (response.success) {
                showNotification('success', response.message);
            } else {
                if (response.redirect) {
                    window.location.href = response.redirect;
                } else {
                    showNotification('danger', response.message);
                }
            }
        },
        error: function() {
            showNotification('danger', 'Error processing your request. Please try again later.');
        }
    });
}

// Helper function to show notification
function showNotification(type, message) {
    const notificationId = 'notification-' + Date.now();
    const html = `
    <div id="${notificationId}" class="alert alert-${type} alert-dismissible fade show" role="alert">
      ${message}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    `;
    
    // Append notification to notification area if exists, otherwise to body
    const notificationArea = document.getElementById('notification-area') || document.body;
    notificationArea.insertAdjacentHTML('beforeend', html);
    
    // Auto-dismiss after 5 seconds
    setTimeout(() => {
        const notification = document.getElementById(notificationId);
        if (notification) {
            notification.classList.remove('show');
            setTimeout(() => notification.remove(), 300);
        }
    }, 5000);
}
