const { VK } = require('vk-io');

const vk = new VK({
  token: 'c4ed1891c4ed1891c4ed1891fcc7f97e91cc4edc4ed1891a093e7194684e04a018d3bb5',
  app: {
    id: '51668480',
    secret: 'SjGpxIqlJwVxO3tTotjT'
  }
});

async function addAchievement(userId, achievementId, title, description, iconId, type, points) {
  try {
    const result = await vk.api.apps.addAchievement({
      user_id: userId,
      achievement_id: achievementId,
      title: title,
      description: description,
      icon_id: iconId,
      type: type,
      points: points
    });

    console.log(result);
  } catch (error) {
    console.error(error);
  }
}

addAchievement(123, 1, 'Покупатель Pro', 'Выдаётся за 50 экологичных покупок', 1, 'points', 100);
addAchievement(123, 1, 'Покупатель Pro Max', 'Выдаётся за 150 экологичных покупок', 1, 'points', 100);