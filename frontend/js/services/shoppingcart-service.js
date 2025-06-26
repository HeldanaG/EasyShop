let cartService;

class ShoppingCartService {
    cart = {
        items: [],
        total: 0
    };

    addToCart(productId) {
        const url = `${config.baseUrl}/cart/products/${productId}`;
        const headers = userService.getHeaders();

        axios.post(url, {}, { headers })
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();
            })
            .catch(error => {
                const data = {
                    error: "Add to cart failed."
                };
                templateBuilder.append("error", data, "errors");
            });
    }

    loadCart() {
        const url = `${config.baseUrl}/cart`;
        const headers = userService.getHeaders();

        axios.get(url, { headers })
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();
            })
            .catch(error => {
                const data = {
                    error: "Load cart failed."
                };
                templateBuilder.append("error", data, "errors");
            });
    }

    setCart(data) {
        this.cart = {
            items: [],
            total: data.total || 0
        };

        for (const [key, value] of Object.entries(data.items)) {
            this.cart.items.push(value);
        }

        this.refreshCartCount(); // 👈 Update header cart count
    }

    clearCart() {
        const url = `${config.baseUrl}/cart`;
        const headers = userService.getHeaders();

        axios.delete(url, { headers })
            .then(() => {
                this.cart = {
                    items: [],
                    total: 0
                };

                this.updateCartDisplay();
                this.refreshCartCount(); // 👈 Update count after clearing
                this.loadCartPage();
            })
            .catch(error => {
                const data = {
                    error: "Empty cart failed."
                };
                templateBuilder.append("error", data, "errors");
            });
    }

    checkout() {
        const url = `${config.baseUrl}/orders`;
        const headers = userService.getHeaders();

        axios.post(url, {}, { headers })
            .then(() => {
                this.cart = {
                    items: [],
                    total: 0
                };

                this.updateCartDisplay();
                this.refreshCartCount(); // 👈 Reset count after order
                this.loadCartPage();

                const msg = document.getElementById("checkout-message");
                msg.style.display = "block";
            })
            .catch(error => {
                const data = {
                    error: "Checkout failed."
                };
                templateBuilder.append("error", data, "errors");
            });
    }

    updateQuantity(productId, delta) {
        const url = `${config.baseUrl}/cart/products/${productId}`;
        const headers = userService.getHeaders();

        const item = this.cart.items.find(i => i.product.productId === productId);
        const newQuantity = item ? item.quantity + delta : 1;

        if (newQuantity <= 0) {
            this.removeItem(productId);
            return;
        }

        axios.put(url, { quantity: newQuantity }, { headers })
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();
                this.loadCartPage();
            })
            .catch(error => {
                const data = {
                    error: "Update quantity failed."
                };
                templateBuilder.append("error", data, "errors");
            });
    }

    removeItem(productId) {
        const url = `${config.baseUrl}/cart/products/${productId}`;
        const headers = userService.getHeaders();

        axios.delete(url, { headers })
            .then(response => {
                this.setCart(response.data);
                this.updateCartDisplay();
                this.loadCartPage();
            })
            .catch(error => {
                const data = {
                    error: "Remove item failed."
                };
                templateBuilder.append("error", data, "errors");
            });
    }

    refreshCartCount() {
        const cartCount = this.cart.items.reduce((sum, item) => sum + item.quantity, 0);
        const cartCountEl = document.getElementById("cart-items");
        if (cartCountEl) {
            cartCountEl.innerText = cartCount;
        }
    }

    loadCartPage() {
        const main = document.getElementById("main");
        main.innerHTML = "";

        let div = document.createElement("div");
        div.classList = "filter-box";
        main.appendChild(div);

        const contentDiv = document.createElement("div");
        contentDiv.id = "content";
        contentDiv.classList.add("content-form");

        const cartHeader = document.createElement("div");
        cartHeader.classList.add("cart-header");

        const h1 = document.createElement("h1");
        h1.innerText = "Cart";
        cartHeader.appendChild(h1);

        const button = document.createElement("button");
        button.classList.add("btn", "btn-danger");
        button.innerText = "Clear";
        button.addEventListener("click", () => this.clearCart());
        cartHeader.appendChild(button);

        contentDiv.appendChild(cartHeader);
        main.appendChild(contentDiv);

        const checkoutDiv = document.createElement("div");
        checkoutDiv.classList.add("text-end", "mt-2");

        const checkoutBtn = document.createElement("button");
        checkoutBtn.id = "checkout-btn";
        checkoutBtn.classList.add("btn", "btn-success");
        checkoutBtn.innerText = "Checkout";
        checkoutBtn.addEventListener("click", () => this.checkout());

        checkoutDiv.appendChild(checkoutBtn);
        contentDiv.appendChild(checkoutDiv);

        const message = document.createElement("div");
        message.id = "checkout-message";
        message.classList.add("mt-2", "text-success");
        message.style.display = "none";
        message.innerHTML = "✅ Your order has been placed!";
        contentDiv.appendChild(message);

        this.cart.items.forEach(item => this.buildItem(item, contentDiv));

        const totalContainer = document.createElement("div");
        totalContainer.classList.add("text-end", "mt-4");

        const totalText = document.createElement("h3");
        totalText.innerText = `🧾 Total Price: $${this.cart.total.toFixed(2)}`;

        totalContainer.appendChild(totalText);
        contentDiv.appendChild(totalContainer);
    }

    buildItem(item, parent) {
        let outerDiv = document.createElement("div");
        outerDiv.classList.add("cart-item");

        let div = document.createElement("div");
        let h4 = document.createElement("h4");
        h4.innerText = item.product.name;
        div.appendChild(h4);
        outerDiv.appendChild(div);

        let photoDiv = document.createElement("div");
        photoDiv.classList.add("photo");
        let img = document.createElement("img");
        img.src = `/images/products/${item.product.imageUrl}`;
        img.addEventListener("click", () => {
            showImageDetailForm(item.product.name, img.src);
        });
        photoDiv.appendChild(img);

        let priceH4 = document.createElement("h4");
        priceH4.classList.add("price");
        priceH4.innerText = `$${item.product.price}`;
        photoDiv.appendChild(priceH4);
        outerDiv.appendChild(photoDiv);

        let descriptionDiv = document.createElement("div");
        descriptionDiv.innerText = item.product.description;
        outerDiv.appendChild(descriptionDiv);

        let quantityDiv = document.createElement("div");
        quantityDiv.classList.add("quantity-controls");

        let minusBtn = document.createElement("button");
        minusBtn.innerText = "-";
        minusBtn.classList.add("btn", "btn-sm", "btn-secondary");
        minusBtn.addEventListener("click", () => {
            cartService.updateQuantity(item.product.productId, -1);
        });

        let quantitySpan = document.createElement("span");
        quantitySpan.innerText = ` ${item.quantity} `;
        quantitySpan.style.margin = "0 10px";

        let plusBtn = document.createElement("button");
        plusBtn.innerText = "+";
        plusBtn.classList.add("btn", "btn-sm", "btn-secondary");
        plusBtn.addEventListener("click", () => {
            cartService.updateQuantity(item.product.productId, 1);
        });

        let removeBtn = document.createElement("button");
        removeBtn.innerText = "🗑";
        removeBtn.classList.add("btn", "btn-sm", "btn-danger", "ms-3");
        removeBtn.addEventListener("click", () => {
            cartService.removeItem(item.product.productId);
        });

        quantityDiv.appendChild(minusBtn);
        quantityDiv.appendChild(quantitySpan);
        quantityDiv.appendChild(plusBtn);
        quantityDiv.appendChild(removeBtn);

        outerDiv.appendChild(quantityDiv);
        parent.appendChild(outerDiv);
    }

    updateCartDisplay() {
        // Optional: implement if you have a summary section outside the cart page
    }
}

cartService = new ShoppingCartService();
