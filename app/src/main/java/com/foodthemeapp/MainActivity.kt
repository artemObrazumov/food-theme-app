package com.foodthemeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.foodthemeapp.components.BottomNavigationBar
import com.foodthemeapp.components.TopAppBar
import com.foodthemeapp.data.Api
import com.foodthemeapp.data.models.Order
import com.foodthemeapp.data.models.Store
import com.foodthemeapp.screen.main.orders.OrdersScreen
import com.foodthemeapp.screen.main.orders.OrdersScreenViewModel
import com.foodthemeapp.screen.main.orders.OrdersScreenViewModelFactory
import com.foodthemeapp.screen.main.products.ProductsScreen
import com.foodthemeapp.screen.main.products.ProductsScreenViewModelFactory
import com.foodthemeapp.screen.main.products.ProductsViewModel
import com.foodthemeapp.screen.main.store.StoreScreen
import com.foodthemeapp.screen.main.store.StoreScreenViewModel
import com.foodthemeapp.ui.theme.Colors
import com.foodthemeapp.ui.theme.FoodThemeAppTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    private val userdata = FoodApp.googleAuthUiClient.getSignedInUser()

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FoodThemeAppTheme {
                var orderedProducts by remember { mutableStateOf(listOf<String>()) }
                FirebaseDatabase.getInstance()
                    .getReference("userOrders/${userdata?.userId ?: ""}")
                    .addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            orderedProducts = (
                                    snapshot.getValue<Map<String, Order>>() ?: emptyMap()
                                    ).map {
                                    println(it)
                                    it.value.productId
                                }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
                    val navController = rememberNavController()
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding(),
                        containerColor = Colors.backgroundYellow,
                        topBar = {
                            TopAppBar(
                                hasOrders = orderedProducts.isNotEmpty(),
                                avatarUrl = userdata?.profilePictureUrl ?: "",
                                username = userdata?.username ?: "Unknown",
                                onLogoutClicked = {
                                    lifecycleScope.launch {
                                        FoodApp.googleAuthUiClient.signOut()
                                        finish()
                                        startActivity(
                                            Intent(
                                                this@MainActivity,
                                                AuthorizationActivity::class.java
                                            )
                                        )
                                    }
                                },
                                onCartClicked = {
                                    navController.navigate(OrdersScreen)
                                }
                            )
                        },
                        bottomBar = {
                            BottomNavigationBar()
                        }
                    ) { innerPadding ->
                        SharedTransitionLayout {
                        NavHost(
                            modifier = Modifier
                                .padding(top = innerPadding.calculateTopPadding()),
                            navController = navController,
                            startDestination = StoreScreen
                        ) {
                            composable<StoreScreen> {
                                val viewModel = viewModel<StoreScreenViewModel>()
                                val state by viewModel.state.collectAsStateWithLifecycle()

                                StoreScreen(
                                    state = state,
                                    animatedVisibilityScope = this,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    onStoreClicked = {
                                        navController.navigate(ProductsScreenNav(
                                            storeId = it.id,
                                            storeIcon = it.icon,
                                            storeName = it.name,
                                            storeAddress = it.address
                                        ))
                                    }
                                )
                            }
                            composable<ProductsScreenNav> {
                                val args = it.toRoute<ProductsScreenNav>()
                                val viewModel = viewModel<ProductsViewModel>(
                                    factory = ProductsScreenViewModelFactory(
                                        storeId = args.storeId,
                                        userId = FoodApp.googleAuthUiClient.getSignedInUser()!!.userId
                                    )
                                )
                                val state by viewModel.state.collectAsStateWithLifecycle()

                                val eventHandler = rememberUpdatedState { owner: LifecycleOwner, event: Lifecycle.Event ->
                                    when(event) {
                                        Lifecycle.Event.ON_RESUME -> {
                                            viewModel.loadProducts()
                                        }
                                        else -> {}
                                    }
                                }
                                val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
                                DisposableEffect(lifecycleOwner.value) {
                                    val lifecycle = lifecycleOwner.value.lifecycle
                                    val observer = LifecycleEventObserver { owner, event ->
                                        eventHandler.value(owner, event)
                                    }

                                    lifecycle.addObserver(observer)
                                    onDispose {
                                        lifecycle.removeObserver(observer)
                                    }
                                }

                                ProductsScreen(
                                    store = Store(
                                        id = args.storeId,
                                        icon = args.storeIcon,
                                        name = args.storeName,
                                        address = args.storeAddress
                                    ),
                                    state = state,
                                    animatedVisibilityScope = this,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    onGoBack = { navController.popBackStack() },
                                    onOrderedItem = { productId ->
                                        viewModel.onProductOrdered(productId)
                                    }
                                )
                            }
                            composable<OrdersScreen> {
                                val viewModel = viewModel<OrdersScreenViewModel>(
                                    factory = OrdersScreenViewModelFactory(
                                        userId = FoodApp.googleAuthUiClient.getSignedInUser()!!.userId
                                    )
                                )
                                val state by viewModel.state.collectAsStateWithLifecycle()

                                OrdersScreen(
                                    state = state,
                                    onCancelOrder = {
                                        viewModel.cancelOrder(
                                            orderId = it
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Serializable
object StoreScreen

@Serializable
data class ProductsScreenNav (
    val storeId: String,
    val storeIcon: String,
    val storeName: String,
    val storeAddress: String
)

@Serializable
object OrdersScreen