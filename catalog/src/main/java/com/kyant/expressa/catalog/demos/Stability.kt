package com.kyant.expressa.catalog.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.expressa.catalog.ui.PageContainer
import com.kyant.expressa.catalog.ui.SectionContainer
import com.kyant.expressa.catalog.ui.Subtitle
import com.kyant.expressa.catalog.ui.TopBar
import com.kyant.expressa.components.button.Button
import com.kyant.expressa.m3.shape.CornerShape
import com.kyant.expressa.prelude.*
import com.kyant.expressa.ui.ProvideTextStyle
import com.kyant.expressa.ui.Text
import kotlin.random.Random

// Stable data class with @Stable annotation
@Stable
data class StableSnackItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double
)

// Unstable data class (no @Stable annotation)
data class UnstableSnackItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double
)

@Composable
fun Stability() {
    var forceRecomposition by remember { mutableIntStateOf(0) }
    
    PageContainer {
        TopBar(
            title = { Text("Stability Demo") }
        )
        
        // Force recomposition button
        SectionContainer {
            Subtitle { Text("Recomposition Control") }
            
            Button(
                onClick = { 
                    forceRecomposition++
                    println("🔄 Force recomposition triggered: $forceRecomposition")
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("强制重组 (Force Recomposition) - Count: $forceRecomposition")
            }
        }
        
        // Single item comparison
        SingleItemComparison(forceRecomposition)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // List comparison
        ListComparison(forceRecomposition)
    }
}

@Composable
private fun SingleItemComparison(forceRecomposition: Int) {
    val stableItem = remember {
        StableSnackItem(1, "Stable Cookie", "This item is stable", 2.50)
    }
    
    val unstableItem = remember {
        UnstableSnackItem(2, "Unstable Cookie", "This item is unstable", 3.00)
    }
    
    SectionContainer {
        Subtitle { Text("单项对比 (Single Item Comparison)") }
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Stable item
            Column(modifier = Modifier.weight(1f)) {
                ProvideTextStyle(titleMedium) {
                    Text(
                        "稳定组件 (Stable)",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                StableSnackItemDisplay(
                    item = stableItem,
                    forceRecomposition = forceRecomposition
                )
            }
            
            // Unstable item  
            Column(modifier = Modifier.weight(1f)) {
                ProvideTextStyle(titleMedium) {
                    Text(
                        "不稳定组件 (Unstable)",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                UnstableSnackItemDisplay(
                    item = unstableItem,
                    forceRecomposition = forceRecomposition
                )
            }
        }
    }
}

@Composable
private fun StableSnackItemDisplay(
    item: StableSnackItem,
    forceRecomposition: Int
) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    
    // Track recompositions
    LaunchedEffect(Unit) {
        recompositionCount++
        println("🟢 StableSnackItem recomposed: $recompositionCount times")
    }
    
    // Simulate some computation to make recomposition cost visible
    val computedValue = remember(item) {
        Thread.sleep(1) // Simulate work
        "Computed: ${item.name.length}"
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CornerShape.medium)
            .background(Color(0xFF4CAF50).copy(alpha = 0.1f))
            .padding(12.dp)
    ) {
        ProvideTextStyle(bodyMedium) {
            Text(item.name)
        }
        ProvideTextStyle(bodySmall) {
            Text(item.description)
            Text("Price: $${item.price}")
            Text("重组次数: $recompositionCount")
            Text(computedValue)
        }
    }
}

@Composable
private fun UnstableSnackItemDisplay(
    item: UnstableSnackItem,
    forceRecomposition: Int
) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    
    // Track recompositions
    LaunchedEffect(Unit) {
        recompositionCount++
        println("🔴 UnstableSnackItem recomposed: $recompositionCount times")
    }
    
    // Simulate some computation to make recomposition cost visible
    val computedValue = remember(item) {
        Thread.sleep(1) // Simulate work
        "Computed: ${item.name.length}"
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CornerShape.medium)
            .background(Color(0xFFF44336).copy(alpha = 0.1f))
            .padding(12.dp)
    ) {
        ProvideTextStyle(bodyMedium) {
            Text(item.name)
        }
        ProvideTextStyle(bodySmall) {
            Text(item.description)
            Text("Price: $${item.price}")
            Text("重组次数: $recompositionCount")
            Text(computedValue)
        }
    }
}

@Composable
private fun ListComparison(forceRecomposition: Int) {
    val stableItems = remember {
        List(10) { index ->
            StableSnackItem(
                id = index,
                name = "Stable Item $index",
                description = "Description for stable item $index",
                price = Random.nextDouble(1.0, 10.0)
            )
        }
    }
    
    val unstableItems = remember {
        List(10) { index ->
            UnstableSnackItem(
                id = index + 100,
                name = "Unstable Item $index",
                description = "Description for unstable item $index", 
                price = Random.nextDouble(1.0, 10.0)
            )
        }
    }
    
    SectionContainer {
        Subtitle { Text("性能对比 (Performance Comparison)") }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Stable list
            Column(modifier = Modifier.weight(1f)) {
                ProvideTextStyle(titleMedium) {
                    Text(
                        "稳定列表 (Stable List)",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Column(
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    stableItems.forEach { item ->
                        StableListItem(item = item, forceRecomposition = forceRecomposition)
                    }
                }
            }
            
            // Unstable list
            Column(modifier = Modifier.weight(1f)) {
                ProvideTextStyle(titleMedium) {
                    Text(
                        "不稳定列表 (Unstable List)",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Column(
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    unstableItems.forEach { item ->
                        UnstableListItem(item = item, forceRecomposition = forceRecomposition)
                    }
                }
            }
        }
    }
}

@Composable
private fun StableListItem(
    item: StableSnackItem,
    forceRecomposition: Int
) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(Unit) {
        recompositionCount++
        if (recompositionCount > 1) {
            println("🟢 StableListItem ${item.id} recomposed: $recompositionCount times")
        }
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CornerShape.small)
            .background(Color(0xFF4CAF50).copy(alpha = 0.05f))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            ProvideTextStyle(bodySmall) {
                Text(item.name)
                Text("$${item.price}")
            }
        }
        ProvideTextStyle(labelSmall) {
            Text("R:$recompositionCount")
        }
    }
}

@Composable
private fun UnstableListItem(
    item: UnstableSnackItem,
    forceRecomposition: Int
) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(Unit) {
        recompositionCount++
        if (recompositionCount > 1) {
            println("🔴 UnstableListItem ${item.id} recomposed: $recompositionCount times")
        }
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CornerShape.small)
            .background(Color(0xFFF44336).copy(alpha = 0.05f))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            ProvideTextStyle(bodySmall) {
                Text(item.name)
                Text("$${item.price}")
            }
        }
        ProvideTextStyle(labelSmall) {
            Text("R:$recompositionCount")
        }
    }
}